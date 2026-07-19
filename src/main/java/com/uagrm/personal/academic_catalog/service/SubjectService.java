package com.uagrm.personal.academic_catalog.service;

import com.uagrm.personal.academic_catalog.dto.SubjectRequestDto;
import com.uagrm.personal.academic_catalog.dto.SubjectResponseDto;
import com.uagrm.personal.academic_catalog.entity.Subject;
import com.uagrm.personal.academic_catalog.mapper.SubjectMapper;
import com.uagrm.personal.academic_catalog.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Transactional
    public SubjectResponseDto createSubject(SubjectRequestDto requestDto) {
        return subjectMapper.toDto(subjectRepository.save(subjectMapper.toEntity(requestDto)));
    }

    @Transactional(readOnly = true)
    public Iterable<SubjectResponseDto> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(subjectMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubjectResponseDto getSubjectById(Long id) {
        return subjectMapper.toDto(subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found")));
    }

    @Transactional
    public SubjectResponseDto updateSubject(Long id, @NonNull SubjectRequestDto requestDto) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found"));
        subject.setName(requestDto.name());
        return subjectMapper.toDto(subjectRepository.save(subject));
    }

    @Transactional
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
