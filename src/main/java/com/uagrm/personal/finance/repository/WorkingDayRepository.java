package com.uagrm.personal.finance.repository;

import com.uagrm.personal.finance.dto.UserWorkingDaySummaryDto;
import com.uagrm.personal.finance.entity.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {

    boolean existsByUserIdAndDate(Long userId, LocalDate date);

    @Query("SELECT w FROM WorkingDay w WHERE w.user.id = :userId AND w.amountWon > w.paidAmount ORDER BY w.date ASC")
    List<WorkingDay> findUnpaidDaysByUserId(Long userId);

    @Query("SELECT w FROM WorkingDay w WHERE w.user.id = :userId ORDER BY w.date DESC")
    List<WorkingDay> findAllByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(w.amountWon - w.paidAmount), 0) FROM WorkingDay w WHERE w.user.id = :userId")
    BigDecimal calculateTotalDeb(Long userId);

    @Query("""
            SELECT new com.uagrm.personal.finance.dto.UserWorkingDaySummaryDto(
                w.user.id, u.username, CONCAT(u.name, ' ', u.lastName),
                COALESCE(SUM(w.paidAmount), 0), COALESCE(SUM(w.amountWon - w.paidAmount), 0))
            FROM WorkingDay w JOIN w.user u
            GROUP BY w.user.id, u.username, u.name, u.lastName
            ORDER BY u.username ASC
            """)
    List<UserWorkingDaySummaryDto> findUserSummaries();
}
