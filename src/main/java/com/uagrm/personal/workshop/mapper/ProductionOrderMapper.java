package com.uagrm.personal.workshop.mapper;

import com.uagrm.personal.workshop.dto.ProductionOrderResponseDto;
import com.uagrm.personal.workshop.entity.ProductionOrder;
import org.springframework.stereotype.Component;

@Component
public class ProductionOrderMapper {
    public ProductionOrderResponseDto toDto(ProductionOrder entity) {
        return new ProductionOrderResponseDto(
                entity.getId(),
                entity.getFrame().getId(),
                entity.getRecipe().getId(),
                entity.getRequestedQuantity(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus()
        );
    }
}
