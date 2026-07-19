package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;

public record RecipeDetailRequestDto(
        Long materialId,
        BigDecimal requiredQuantity
) {
}
