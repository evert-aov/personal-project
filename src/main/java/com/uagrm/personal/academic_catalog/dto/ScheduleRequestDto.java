package com.uagrm.personal.academic_catalog.dto;

import java.time.LocalTime;

public record ScheduleRequestDto(
        LocalTime startTime,
        LocalTime endTime
) {
}
