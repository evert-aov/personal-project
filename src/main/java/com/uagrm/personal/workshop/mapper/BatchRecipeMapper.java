package com.uagrm.personal.workshop.mapper;

import com.uagrm.personal.workshop.dto.BatchRecipeResponseDto;
import com.uagrm.personal.workshop.entity.BatchRecipe;
import org.springframework.stereotype.Component;

@Component
public class BatchRecipeMapper {
    public BatchRecipeResponseDto toDto(BatchRecipe entity) {
        return new BatchRecipeResponseDto(
                entity.getId(),
                entity.getFrame().getId(),
                entity.getBatchQuantity(),
                entity.getDescription()
        );
    }
}
