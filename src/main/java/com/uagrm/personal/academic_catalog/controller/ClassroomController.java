package com.uagrm.personal.academic_catalog.controller;

import com.uagrm.personal.academic_catalog.dto.ClassroomRequestDto;
import com.uagrm.personal.academic_catalog.dto.ClassroomResponseDto;
import com.uagrm.personal.academic_catalog.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping
    public ResponseEntity<ClassroomResponseDto> createClassroom(@RequestBody ClassroomRequestDto requestDto) {
        return new ResponseEntity<>(classroomService.classroomCreate(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<ClassroomResponseDto>> getAllClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDto> getClassroomById(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponseDto> updateClassroom(@PathVariable Long id, @RequestBody ClassroomRequestDto requestDto) {
        return ResponseEntity.ok(classroomService.updateClassroom(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }
}