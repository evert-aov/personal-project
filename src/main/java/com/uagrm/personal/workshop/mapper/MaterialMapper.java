package com.uagrm.personal.workshop.mapper;

import com.uagrm.personal.workshop.dto.MaterialRequestDto;
import com.uagrm.personal.workshop.dto.MaterialResponseDto;
import com.uagrm.personal.workshop.entity.Material;
import org.springframework.stereotype.Component;

@Component
public class MaterialMapper {
    public Material toEntity(MaterialRequestDto dto) {
        if (dto == null) return null;

        return Material.builder()
                .name(dto.name())
                .type(dto.type())
                .dimensions(dto.dimensions())
                .unitOfMeasure(dto.unitOfMeasure())
                .minStock(dto.minStock())
                .unitPrice(dto.unitPrice() != null ? dto.unitPrice() : java.math.BigDecimal.ZERO)
                .build();
    }

    public MaterialResponseDto toDto(Material entity) {
        return new MaterialResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getDimensions(),
                entity.getUnitOfMeasure(),
                entity.getMinStock(),
                entity.getCurrentStock(),
                entity.getUnitPrice() != null ? entity.getUnitPrice() : java.math.BigDecimal.ZERO
        );
    }
}
