package com.uagrm.schedule_assignment.academic_catalog.mapper;

import com.uagrm.schedule_assignment.academic_catalog.dto.SubjectRequestDto;
import com.uagrm.schedule_assignment.academic_catalog.dto.SubjectResponseDto;
import com.uagrm.schedule_assignment.academic_catalog.entity.Subject;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubjectMapper {
    public Subject toEntity(SubjectRequestDto requestDto) {
        if (requestDto == null) return null;

        return Subject.builder()
                .code(requestDto.code())
                .name(requestDto.name())
                .build();
    }

    public SubjectResponseDto toDto(@NonNull Subject subject) {
        return new SubjectResponseDto(
                subject.getId(),
                subject.getCode(),
                subject.getName());
    }
}
