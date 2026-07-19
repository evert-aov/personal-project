package com.uagrm.personal.finance.dto;

import com.uagrm.personal.finance.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDto(
        String description,
        LocalDate date,
        TransactionType type,
        BigDecimal amount
) {
}
