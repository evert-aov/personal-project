package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;

public record PurchaseDetailResponseDto(
        Long id,
        Long purchaseId,
        Long materialId,
        BigDecimal quantityPurchased,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
