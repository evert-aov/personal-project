package com.uagrm.schedule_assignment.finance.repository;

import com.uagrm.schedule_assignment.finance.entity.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {
}
