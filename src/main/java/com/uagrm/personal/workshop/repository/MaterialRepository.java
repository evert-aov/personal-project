package com.uagrm.personal.workshop.repository;

import com.uagrm.personal.workshop.entity.Material;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    // Mirrors the row-level "FOR UPDATE" the original fn_stock_subtract_usage trigger used,
    // so concurrent stock updates don't produce lost updates.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Material m WHERE m.id = :id")
    Optional<Material> findByIdForUpdate(Long id);
}
