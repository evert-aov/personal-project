package com.uagrm.schedule_assignment.finance.repository;

import com.uagrm.schedule_assignment.finance.entity.Transaction;
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

    @Query("select SUM(t.amount - t.paidAmount ) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'LOAN'")
    BigDecimal calculateTotalDeb(Long userId);
}
