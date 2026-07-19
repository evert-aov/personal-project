package com.uagrm.personal.academic_catalog.mapper;

import com.uagrm.personal.academic_catalog.dto.ClassroomRequestDto;
import com.uagrm.personal.academic_catalog.dto.ClassroomResponseDto;
import com.uagrm.personal.academic_catalog.entity.Classroom;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassroomMapper {
    public Classroom toEntity(ClassroomRequestDto dto) {
        if (dto == null) return null;

        return Classroom.builder()
                .name(dto.name())
                .capacity(dto.capacity())
                .build();
    }

    public ClassroomResponseDto toDto(@NonNull Classroom classroom) {
        return new ClassroomResponseDto(
                classroom.getId(),
                classroom.getName(),
                classroom.getCapacity());
    }
}
