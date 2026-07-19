package com.uagrm.personal.workshop.mapper;

import com.uagrm.personal.workshop.dto.PurchaseResponseDto;
import com.uagrm.personal.workshop.entity.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {
    public PurchaseResponseDto toDto(Purchase entity) {
        return new PurchaseResponseDto(
                entity.getId(),
                entity.getPurchaseDate(),
                entity.getSupplier(),
                entity.getTotal()
        );
    }
}
