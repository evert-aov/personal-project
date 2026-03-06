package com.uagrm.schedule_assignment.academic_catalog.dto;

import java.time.LocalDate;

public record AcademicPeriodRequestDto(
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
}
