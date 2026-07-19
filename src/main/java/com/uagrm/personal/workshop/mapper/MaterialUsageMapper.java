package com.uagrm.personal.workshop.mapper;

import com.uagrm.personal.workshop.dto.MaterialUsageResponseDto;
import com.uagrm.personal.workshop.entity.MaterialUsage;
import org.springframework.stereotype.Component;

@Component
public class MaterialUsageMapper {
    public MaterialUsageResponseDto toDto(MaterialUsage entity) {
        return new MaterialUsageResponseDto(
                entity.getId(),
                entity.getOrder().getId(),
                entity.getMaterial().getId(),
                entity.getUsageDate(),
                entity.getQuantityUsed()
        );
    }
}
