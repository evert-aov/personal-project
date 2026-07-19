package com.uagrm.personal.workshop.dto;

import java.time.LocalDate;

public record ProductionOrderResponseDto(
        Long id,
        Long frameId,
        Long recipeId,
        Integer requestedQuantity,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {
}
