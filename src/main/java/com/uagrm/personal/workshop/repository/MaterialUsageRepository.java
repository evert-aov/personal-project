package com.uagrm.personal.workshop.repository;

import com.uagrm.personal.workshop.entity.MaterialUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialUsageRepository extends JpaRepository<MaterialUsage, Long> {
    List<MaterialUsage> findAllByOrderId(Long orderId);
}
