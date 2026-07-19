package com.uagrm.personal.workshop.dto;

import java.time.LocalDate;

public record ProductionOrderRequestDto(
        Long frameId,
        Long recipeId,
        Integer requestedQuantity,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {
}
