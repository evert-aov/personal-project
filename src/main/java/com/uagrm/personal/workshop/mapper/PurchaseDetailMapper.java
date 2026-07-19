package com.uagrm.personal.workshop.mapper;

import com.uagrm.personal.workshop.dto.PurchaseDetailResponseDto;
import com.uagrm.personal.workshop.entity.PurchaseDetail;
import org.springframework.stereotype.Component;

@Component
public class PurchaseDetailMapper {
    public PurchaseDetailResponseDto toDto(PurchaseDetail entity) {
        return new PurchaseDetailResponseDto(
                entity.getId(),
                entity.getPurchase().getId(),
                entity.getMaterial().getId(),
                entity.getQuantityPurchased(),
                entity.getUnitPrice(),
                entity.getSubtotal()
        );
    }
}
