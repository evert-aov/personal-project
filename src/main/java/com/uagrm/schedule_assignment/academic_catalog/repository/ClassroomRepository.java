package com.uagrm.schedule_assignment.academic_catalog.repository;

import com.uagrm.schedule_assignment.academic_catalog.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
