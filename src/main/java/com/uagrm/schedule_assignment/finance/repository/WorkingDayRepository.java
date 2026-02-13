package com.uagrm.schedule_assignment.finance.repository;

import com.uagrm.schedule_assignment.finance.entity.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {

    @Query("SELECT w FROM WorkingDay w WHERE w.user.id = :userId AND w.amountWon > w.paidAmount ORDER BY w.date ASC")
    List<WorkingDay> findUnpaidDaysByUserId(Long userId);

    @Query("SELECT SUM(w.amountWon - w.paidAmount) FROM WorkingDay w WHERE w.user.id = :userId")
    BigDecimal calculateTotalDeb(Long userId);
}
