package com.uagrm.schedule_assignment.academic_catalog.controller;

import com.uagrm.schedule_assignment.academic_catalog.dto.SubjectRequestDto;
import com.uagrm.schedule_assignment.academic_catalog.dto.SubjectResponseDto;
import com.uagrm.schedule_assignment.academic_catalog.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectResponseDto> createSubject(@RequestBody SubjectRequestDto requestDto){
        return new ResponseEntity<>(subjectService.createSubject(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<SubjectResponseDto>> getAllSubjects(){
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> getSubjectById(@PathVariable Long id){
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> updateSubject(@PathVariable Long id, @RequestBody SubjectRequestDto requestDto){
        return ResponseEntity.ok(subjectService.updateSubject(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id){
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}
