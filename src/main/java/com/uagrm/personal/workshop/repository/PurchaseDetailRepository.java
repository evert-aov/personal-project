package com.uagrm.personal.workshop.repository;

import com.uagrm.personal.workshop.entity.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail, Long> {
    List<PurchaseDetail> findAllByPurchaseId(Long purchaseId);
}
