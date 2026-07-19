package com.uagrm.personal.finance.dto;

import com.uagrm.personal.finance.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDto (
        Long id,
        Long userId,
        String description,
        TransactionType type,
        LocalDate date,
        BigDecimal amount,
        BigDecimal paidAmount
) {
}
