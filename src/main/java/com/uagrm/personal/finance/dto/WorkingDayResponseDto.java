package com.uagrm.personal.finance.dto;

import com.uagrm.personal.finance.entity.DayStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WorkingDayResponseDto(
        Long id,
        Long userId,
        LocalDate date,
        DayStatus status,
        BigDecimal amountWon,
        BigDecimal paidAmount
) {}
