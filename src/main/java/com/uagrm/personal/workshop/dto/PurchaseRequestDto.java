package com.uagrm.personal.workshop.dto;

import java.time.LocalDate;

public record PurchaseRequestDto(
        LocalDate purchaseDate,
        String supplier
) {
}
