package com.uagrm.personal.finance.dto;

import com.uagrm.personal.finance.entity.DayStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WorkingDayRequestDto(
        LocalDate date,
        DayStatus status,
        BigDecimal amountWon
) {
}
