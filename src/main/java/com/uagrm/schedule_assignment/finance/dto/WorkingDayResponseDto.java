package com.uagrm.schedule_assignment.finance.dto;

import com.uagrm.schedule_assignment.finance.entity.DayStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WorkingDayResponseDto(
        Long id,
        Long userId,
        LocalDate date,
        DayStatus status,
        BigDecimal amountWon
) {}
