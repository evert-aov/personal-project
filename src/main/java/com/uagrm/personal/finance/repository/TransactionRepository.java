package com.uagrm.personal.finance.repository;

import com.uagrm.personal.finance.dto.UserTransactionSummaryDto;
import com.uagrm.personal.finance.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserId(Long userId);

    // Busca transacciones de tipo LOAN (préstamo) que no se han terminado de pagar
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.type = 'LOAN' AND t.amount > t.paidAmount ORDER BY t.date ASC")
    List<Transaction> findUnpaidLoansByUserId(Long userId);

    @Query("select COALESCE(SUM(t.amount - t.paidAmount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'LOAN'")
    BigDecimal calculateTotalDeb(Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.type IN ('PAYMENT', 'LOAN') ORDER BY t.date DESC")
    List<Transaction> findPaymentsAndLoansByUserId(Long userId);

    @Query("""
            SELECT new com.uagrm.personal.finance.dto.UserTransactionSummaryDto(
                t.user.id, u.username, CONCAT(u.name, ' ', u.lastName),
                COALESCE(SUM(CASE WHEN t.type = 'PAYMENT' THEN t.amount ELSE 0 END), 0),
                COALESCE(SUM(CASE WHEN t.type = 'LOAN' THEN t.amount - t.paidAmount ELSE 0 END), 0))
            FROM Transaction t JOIN t.user u
            WHERE t.type IN ('PAYMENT', 'LOAN')
            GROUP BY t.user.id, u.username, u.name, u.lastName
            ORDER BY u.username ASC
            """)
    List<UserTransactionSummaryDto> findUserSummaries();
}
