package com.uagrm.schedule_assignment.academic_catalog.dto;

import java.time.LocalTime;

public record ScheduleResponseDto(
        Long id,
        LocalTime startTime,
        LocalTime endTime
) {
}
