package com.uagrm.personal.academic_catalog.mapper;

import com.uagrm.personal.academic_catalog.dto.ScheduleRequestDto;
import com.uagrm.personal.academic_catalog.dto.ScheduleResponseDto;
import com.uagrm.personal.academic_catalog.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {
    public Schedule toEntity(ScheduleRequestDto dto) {
        if (dto == null) return null;

        return Schedule.builder()
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .build();
    }

    public ScheduleResponseDto toDto(@NonNull Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getStartTime(),
                schedule.getEndTime());
    }
}
