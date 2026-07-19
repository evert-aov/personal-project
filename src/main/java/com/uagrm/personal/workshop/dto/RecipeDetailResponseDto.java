package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;

public record RecipeDetailResponseDto(
        Long id,
        Long recipeId,
        Long materialId,
        BigDecimal requiredQuantity
) {
}
