package com.uagrm.schedule_assignment.academic_catalog.mapper;

import com.uagrm.schedule_assignment.academic_catalog.dto.AcademicPeriodRequestDto;
import com.uagrm.schedule_assignment.academic_catalog.dto.AcademicPeriodResponseDto;
import com.uagrm.schedule_assignment.academic_catalog.entity.AcademicPeriod;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AcademicMapper {
    public AcademicPeriod toEntity(AcademicPeriodRequestDto requestDto) {
        if (requestDto == null) return null;

        return AcademicPeriod.builder()
                .name(requestDto.name())
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .build();
    }

    public AcademicPeriodResponseDto toDto(@NonNull AcademicPeriod academicPeriod) {
        return new AcademicPeriodResponseDto(
                academicPeriod.getId(),
                academicPeriod.getName(),
                academicPeriod.getStartDate(),
                academicPeriod.getEndDate());
    }
}
