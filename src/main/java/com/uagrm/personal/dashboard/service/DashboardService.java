package com.uagrm.personal.dashboard.service;

import com.uagrm.personal.dashboard.dto.AdminUserWorkSummaryDto;
import com.uagrm.personal.dashboard.dto.PersonalDashboardDto;
import com.uagrm.personal.dashboard.dto.WorkSummaryDto;
import com.uagrm.personal.finance.entity.Transaction;
import com.uagrm.personal.finance.entity.TransactionType;
import com.uagrm.personal.finance.entity.WorkingDay;
import com.uagrm.personal.finance.repository.TransactionRepository;
import com.uagrm.personal.finance.repository.WorkingDayRepository;
import com.uagrm.personal.security.entity.User;
import com.uagrm.personal.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final UserRepository userRepository;
    private final WorkingDayRepository workingDayRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public PersonalDashboardDto getPersonalSummary(User user, LocalDate from, LocalDate to) {
        WorkSummaryDto work = getWorkSummary(user, from, to);

        List<Transaction> transactions = transactionRepository.findAllByUserId(user.getId());
        List<Transaction> scoped = (from == null || to == null) ? transactions : inRange(transactions, from, to, Transaction::getDate);

        BigDecimal totalIncome = sum(scoped, TransactionType.INCOME, Transaction::getAmount);
        BigDecimal totalExpense = sum(scoped, TransactionType.EXPENSE, Transaction::getAmount);

        BigDecimal currentBalance = totalIncome
                .add(work.totalPaidDays())
                .add(work.advanceBalance())
                .subtract(work.loanDebt())
                .subtract(totalExpense);

        return new PersonalDashboardDto(work, totalIncome, totalExpense, currentBalance);
    }

    @Transactional(readOnly = true)
    public List<AdminUserWorkSummaryDto> getAdminWorkSummaries(LocalDate from, LocalDate to) {
        return userRepository.findAll().stream()
                .map(user -> toAdminDto(user, getWorkSummary(user, from, to)))
                .toList();
    }

    /**
     * Work-only snapshot (days + loans + advance), no income/expense/balance -- used both by the
     * personal dashboard's "work" card group and the admin per-user summary/drill-down.
     *
     * With no date range, this trusts the persisted running totals (paidAmount, advanceBalance)
     * exactly like the rest of the app. With a range, those persisted totals mix in money that
     * moved outside the window, so instead it re-simulates the same oldest-first FIFO settlement
     * (see TransactionService#distributePayment) using only records dated inside the range --
     * an isolated ledger for just that period.
     */
    private WorkSummaryDto getWorkSummary(User user, LocalDate from, LocalDate to) {
        List<WorkingDay> days = workingDayRepository.findAllByUserId(user.getId());
        List<Transaction> transactions = transactionRepository.findAllByUserId(user.getId());
        List<Transaction> loans = transactions.stream().filter(t -> t.getType() == TransactionType.LOAN).toList();

        if (from == null || to == null) {
            BigDecimal totalEarned = sumAmounts(days, WorkingDay::getAmountWon);
            BigDecimal totalPaidDays = sumAmounts(days, WorkingDay::getPaidAmount);
            BigDecimal loanDebt = loans.stream().map(Transaction::getRemainingAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            return new WorkSummaryDto(
                    days.size(), totalEarned, totalPaidDays,
                    totalEarned.subtract(totalPaidDays), loanDebt, user.getAdvanceBalance()
            );
        }

        List<Transaction> payments = transactions.stream().filter(t -> t.getType() == TransactionType.PAYMENT).toList();
        List<WorkingDay> periodDays = inRange(days, from, to, WorkingDay::getDate);
        List<Transaction> periodLoans = inRange(loans, from, to, Transaction::getDate);
        List<Transaction> periodPayments = inRange(payments, from, to, Transaction::getDate);

        return simulatePeriod(periodDays, periodLoans, periodPayments);
    }

    private WorkSummaryDto simulatePeriod(List<WorkingDay> days, List<Transaction> loans, List<Transaction> payments) {
        BigDecimal totalEarned = sumAmounts(days, WorkingDay::getAmountWon);
        BigDecimal totalLoanAmount = sumAmounts(loans, Transaction::getAmount);
        BigDecimal totalPaymentAmount = sumAmounts(payments, Transaction::getAmount);

        List<Object> debts = new ArrayList<>();
        debts.addAll(days);
        debts.addAll(loans);
        debts.sort(Comparator.comparing(item -> item instanceof WorkingDay d ? d.getDate() : ((Transaction) item).getDate()));

        Map<Object, BigDecimal> paidByItem = new HashMap<>();
        BigDecimal remaining = totalPaymentAmount;
        for (Object item : debts) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal debtAmount = item instanceof WorkingDay d ? d.getAmountWon() : ((Transaction) item).getAmount();
            BigDecimal applied = remaining.min(debtAmount);
            paidByItem.put(item, applied);
            remaining = remaining.subtract(applied);
        }

        BigDecimal totalPaidDays = days.stream().map(d -> paidByItem.getOrDefault(d, BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPaidLoans = loans.stream().map(l -> paidByItem.getOrDefault(l, BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal advance = remaining.max(BigDecimal.ZERO);

        return new WorkSummaryDto(
                days.size(), totalEarned, totalPaidDays,
                totalEarned.subtract(totalPaidDays), totalLoanAmount.subtract(totalPaidLoans), advance
        );
    }

    private <T> List<T> inRange(List<T> items, LocalDate from, LocalDate to, Function<T, LocalDate> dateOf) {
        return items.stream()
                .filter(item -> !dateOf.apply(item).isBefore(from) && !dateOf.apply(item).isAfter(to))
                .toList();
    }

    private <T> BigDecimal sumAmounts(List<T> items, Function<T, BigDecimal> amountOf) {
        return items.stream().map(amountOf).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sum(List<Transaction> items, TransactionType type, Function<Transaction, BigDecimal> amountOf) {
        return items.stream().filter(t -> t.getType() == type).map(amountOf).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private AdminUserWorkSummaryDto toAdminDto(User user, WorkSummaryDto work) {
        return new AdminUserWorkSummaryDto(
                user.getId(), user.getUsername(), user.getName() + " " + user.getLastName(),
                work.workingDaysCount(), work.totalEarned(), work.totalPaidDays(),
                work.daysDebt(), work.loanDebt(), work.advanceBalance()
        );
    }
}
