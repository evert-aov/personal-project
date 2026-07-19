package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;

public record MaterialResponseDto(
        Long id,
        String name,
        String type,
        String dimensions,
        String unitOfMeasure,
        BigDecimal minStock,
        BigDecimal currentStock,
        BigDecimal unitPrice
) {
}
