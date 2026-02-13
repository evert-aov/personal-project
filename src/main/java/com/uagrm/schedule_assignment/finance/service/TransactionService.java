package com.uagrm.schedule_assignment.finance.service;

import com.uagrm.schedule_assignment.finance.dto.TransactionRequestDto;
import com.uagrm.schedule_assignment.finance.dto.TransactionResponseDto;
import com.uagrm.schedule_assignment.finance.entity.Transaction;
import com.uagrm.schedule_assignment.finance.entity.TransactionType;
import com.uagrm.schedule_assignment.finance.entity.WorkingDay;
import com.uagrm.schedule_assignment.finance.mapper.TransactionMapper;
import com.uagrm.schedule_assignment.finance.repository.TransactionRepository;
import com.uagrm.schedule_assignment.finance.repository.WorkingDayRepository;
import com.uagrm.schedule_assignment.security.entity.User;
import com.uagrm.schedule_assignment.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final WorkingDayRepository workingDayRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponseDto addMyTransaction(TransactionRequestDto requestDto) {
        User userCurrent = userService.getCurrentUser();


        Transaction transaction = transactionMapper.toEntity(requestDto);
        transaction.setUser(userCurrent);

        if (transaction.getType() == TransactionType.RECEIVED)
            distributePayment(userCurrent, transaction.getAmount());

        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    /**
     * Este método se encarga de buscar deudas viejas (Días trabajados o Préstamos)
     * y "matarlas" con el monto que está entrando.
     */
    private void distributePayment(User user, BigDecimal amountAvailable) {
        BigDecimal remainingMoney = amountAvailable;

        // Obtener días trabajados no pagados
        List<WorkingDay> unpaidDays = workingDayRepository.findUnpaidDaysByUserId(user.getId());

        // Obtener préstamos que hiciste a la empresa y no te devolvieron
        List<Transaction> unpaidLoans = transactionRepository.findUnpaidLoansByUserId(user.getId());

        // Unificar y Ordenar por fecha (el más antiguo primero)
        List<Object> allDebts = new java.util.ArrayList<>();
        allDebts.addAll(unpaidDays);
        allDebts.addAll(unpaidLoans);

        allDebts.sort((a, b) -> {
            LocalDate dateA = (a instanceof WorkingDay) ? ((WorkingDay) a).getDate() : ((Transaction) a).getDate();
            LocalDate dateB = (b instanceof WorkingDay) ? ((WorkingDay) b).getDate() : ((Transaction) b).getDate();
            return dateA.compareTo(dateB);
        });

        // Bucle de pago
        for (Object debtItem : allDebts) {
            if (remainingMoney.compareTo(BigDecimal.ZERO) <= 0) break;

            if (debtItem instanceof WorkingDay day) {
                // Pagar Día Trabajado
                BigDecimal debt = day.getRemainingAmount();

                if (remainingMoney.compareTo(debt) >= 0) {
                    day.setPaidAmount(day.getAmountWon()); // Pagado total
                    remainingMoney = remainingMoney.subtract(debt);
                } else {
                    day.setPaidAmount(day.getPaidAmount().add(remainingMoney)); // Pagado parcial
                    remainingMoney = BigDecimal.ZERO;
                }
                workingDayRepository.save(day);

            } else if (debtItem instanceof Transaction loan) {
                // Pagar Préstamo (LOAN)
                BigDecimal debt = loan.getRemainingAmount();

                if (remainingMoney.compareTo(debt) >= 0) {
                    loan.setPaidAmount(loan.getAmount()); // Devuelto total
                    remainingMoney = remainingMoney.subtract(debt);
                } else {
                    loan.setPaidAmount(loan.getPaidAmount().add(remainingMoney)); // Devuelto parcial
                    remainingMoney = BigDecimal.ZERO;
                }
                transactionRepository.save(loan);
            }
        }
    }

    @Transactional(readOnly = true)
    public Iterable<TransactionResponseDto> getAllMyTransactions() {
        User userCurrent = userService.getCurrentUser();
        return transactionRepository.findAllByUserId(userCurrent.getId())
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Transactional
    public TransactionResponseDto getMyTransactionById(Long id) {
        User userCurrent = userService.getCurrentUser();
        return transactionRepository.findById(id)
                .filter(transaction -> transaction.getUser().getId().equals(userCurrent.getId()))
                .map(transactionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Transactional
    public TransactionResponseDto updateTransaction(Long id, TransactionRequestDto requestDto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        User userCurrent = userService.getCurrentUser();
        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !transaction.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to update this record.");

        transaction.setAmount(requestDto.amount());
        transaction.setDescription(requestDto.description());
        transaction.setType(requestDto.type());
        transaction.setDate(requestDto.date());


        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Transactional
    public void deleteTransaction(Long id) {
        User userCurrent = userService.getCurrentUser();
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !transaction.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to delete this record.");

        transactionRepository.delete(transaction);
    }

    @Transactional
    public BigDecimal totalDebt() {
        User userCurrent = userService.getCurrentUser();

        return transactionRepository.calculateTotalDeb(userCurrent.getId());
    }
}
