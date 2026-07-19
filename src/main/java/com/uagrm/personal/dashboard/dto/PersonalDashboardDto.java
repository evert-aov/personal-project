package com.uagrm.personal.dashboard.dto;

import java.math.BigDecimal;

public record PersonalDashboardDto(
        WorkSummaryDto work,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal currentBalance
) {}
