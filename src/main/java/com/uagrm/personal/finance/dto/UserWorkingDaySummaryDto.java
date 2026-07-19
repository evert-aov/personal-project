package com.uagrm.personal.finance.dto;

import java.math.BigDecimal;

public record UserWorkingDaySummaryDto(
        Long userId,
        String username,
        String fullName,
        BigDecimal totalPaid,
        BigDecimal totalDebt
) {}
