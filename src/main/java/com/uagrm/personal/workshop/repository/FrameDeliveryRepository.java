package com.uagrm.personal.workshop.repository;

import com.uagrm.personal.workshop.entity.FrameDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrameDeliveryRepository extends JpaRepository<FrameDelivery, Long> {
    List<FrameDelivery> findAllByOrderId(Long orderId);
}
