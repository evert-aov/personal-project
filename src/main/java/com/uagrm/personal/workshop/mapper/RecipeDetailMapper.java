package com.uagrm.personal.workshop.mapper;

import com.uagrm.personal.workshop.dto.RecipeDetailResponseDto;
import com.uagrm.personal.workshop.entity.RecipeDetail;
import org.springframework.stereotype.Component;

@Component
public class RecipeDetailMapper {
    public RecipeDetailResponseDto toDto(RecipeDetail entity) {
        return new RecipeDetailResponseDto(
                entity.getId(),
                entity.getRecipe().getId(),
                entity.getMaterial().getId(),
                entity.getRequiredQuantity()
        );
    }
}
