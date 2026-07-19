package com.uagrm.personal.workshop.dto;

public record BatchRecipeResponseDto(
        Long id,
        Long frameId,
        Integer batchQuantity,
        String description
) {
}
