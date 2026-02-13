package com.uagrm.schedule_assignment.finance.dto;

import com.uagrm.schedule_assignment.finance.entity.TransactionType;

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
