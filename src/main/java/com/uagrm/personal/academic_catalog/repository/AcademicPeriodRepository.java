package com.uagrm.personal.academic_catalog.repository;

import com.uagrm.personal.academic_catalog.entity.AcademicPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicPeriodRepository extends JpaRepository<AcademicPeriod, Long> {
}
