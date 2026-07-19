package com.uagrm.personal.dashboard.dto;

import java.math.BigDecimal;

public record AdminUserWorkSummaryDto(
        Long userId,
        String username,
        String fullName,
        long workingDaysCount,
        BigDecimal totalEarned,
        BigDecimal totalPaidDays,
        BigDecimal daysDebt,
        BigDecimal loanDebt,
        BigDecimal advanceBalance
) {}
