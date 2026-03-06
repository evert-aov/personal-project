package com.uagrm.schedule_assignment.academic_catalog.dto;

import java.time.LocalTime;

public record ScheduleRequestDto(
        LocalTime startTime,
        LocalTime endTime
) {
}
