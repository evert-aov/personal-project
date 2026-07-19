package com.uagrm.personal.academic_catalog.dto;

import java.time.LocalDate;

public record AcademicPeriodRequestDto(
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
}
