package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;

public record PurchaseDetailRequestDto(
        Long materialId,
        BigDecimal quantityPurchased,
        BigDecimal unitPrice
) {
}
