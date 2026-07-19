package com.uagrm.personal.dashboard.dto;

import java.math.BigDecimal;

public record WorkSummaryDto(
        long workingDaysCount,
        BigDecimal totalEarned,
        BigDecimal totalPaidDays,
        BigDecimal daysDebt,
        BigDecimal loanDebt,
        BigDecimal advanceBalance
) {}
