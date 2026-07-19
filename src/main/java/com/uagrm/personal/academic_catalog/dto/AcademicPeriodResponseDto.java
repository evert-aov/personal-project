package com.uagrm.personal.academic_catalog.dto;

import java.time.LocalDate;

public record AcademicPeriodResponseDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
}
