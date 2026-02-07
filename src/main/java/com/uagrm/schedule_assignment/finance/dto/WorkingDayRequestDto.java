package com.uagrm.schedule_assignment.finance.dto;

import com.uagrm.schedule_assignment.finance.entity.DayStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WorkingDayRequestDto(
        LocalDate date,
        DayStatus status,
        BigDecimal amountWon
) {
}
