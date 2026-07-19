package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MaterialUsageResponseDto(
        Long id,
        Long orderId,
        Long materialId,
        LocalDate usageDate,
        BigDecimal quantityUsed
) {
}
