package com.uagrm.personal.finance.service;

import com.uagrm.personal.security.entity.User;
import com.uagrm.personal.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Tracks each user's running credit balance: money already paid to them (via a PAYMENT
 * transaction) that hasn't been consumed by a debt yet (an unpaid WorkingDay or LOAN).
 * A PAYMENT that exceeds the debts it settles banks the leftover here; new debts drain
 * it automatically as they're created, so an advance keeps covering future work/loans
 * without the admin having to re-enter anything.
 */
@Service
@RequiredArgsConstructor
public class AdvanceBalanceService {
    private final UserRepository userRepository;

    @Transactional
    public void addCredit(User user, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return;

        user.setAdvanceBalance(user.getAdvanceBalance().add(amount));
        userRepository.save(user);
    }

    /**
     * Consumes as much of the user's available credit as needed to cover {@code debtAmount},
     * decrementing the balance, and returns the amount actually applied.
     */
    @Transactional
    public BigDecimal applyAvailableCredit(User user, BigDecimal debtAmount) {
        BigDecimal available = user.getAdvanceBalance();
        if (available == null || available.compareTo(BigDecimal.ZERO) <= 0
                || debtAmount == null || debtAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal applied = available.min(debtAmount);
        user.setAdvanceBalance(available.subtract(applied));
        userRepository.save(user);
        return applied;
    }
}
