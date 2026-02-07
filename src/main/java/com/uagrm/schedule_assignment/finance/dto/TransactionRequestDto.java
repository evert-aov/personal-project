package com.uagrm.schedule_assignment.finance.dto;

import com.uagrm.schedule_assignment.finance.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDto(
        Long workingDayId,
        String description,
        LocalDate date,
        TransactionType type,
        BigDecimal amount
) {
}
