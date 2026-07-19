package com.uagrm.personal.workshop.dto;

public record BatchRecipeRequestDto(
        Long frameId,
        Integer batchQuantity,
        String description
) {
}
