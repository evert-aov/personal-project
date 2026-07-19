package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MaterialUsageRequestDto(
        Long materialId,
        LocalDate usageDate,
        BigDecimal quantityUsed
) {
}
