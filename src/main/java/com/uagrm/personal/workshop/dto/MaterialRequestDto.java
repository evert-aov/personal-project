package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;

public record MaterialRequestDto(
        String name,
        String type,
        String dimensions,
        String unitOfMeasure,
        BigDecimal minStock,
        BigDecimal unitPrice
) {
}
