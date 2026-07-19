package com.uagrm.personal.academic_catalog.service;

import com.uagrm.personal.academic_catalog.dto.ClassroomRequestDto;
import com.uagrm.personal.academic_catalog.dto.ClassroomResponseDto;
import com.uagrm.personal.academic_catalog.entity.Classroom;
import com.uagrm.personal.academic_catalog.mapper.ClassroomMapper;
import com.uagrm.personal.academic_catalog.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassroomMapper classroomMapper;

    @Transactional
    public ClassroomResponseDto classroomCreate(ClassroomRequestDto requestDto) {
        return classroomMapper.toDto(classroomRepository.save(classroomMapper.toEntity(requestDto)));
    }

    @Transactional(readOnly = true)
    public Iterable<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll()
                .stream()
                .map(classroomMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClassroomResponseDto getClassroomById(Long id) {
        return classroomMapper.toDto(classroomRepository.findById(id).orElseThrow(() -> new RuntimeException("Classroom not found")));
    }

    @Transactional
    public ClassroomResponseDto updateClassroom(Long id, @NonNull ClassroomRequestDto requestDto) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        classroom.setName(requestDto.name());
        return classroomMapper.toDto(classroomRepository.save(classroom));
    }

    @Transactional
    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }
}
