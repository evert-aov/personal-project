package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseResponseDto(
        Long id,
        LocalDate purchaseDate,
        String supplier,
        BigDecimal total
) {
}
