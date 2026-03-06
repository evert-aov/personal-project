package com.uagrm.schedule_assignment.academic_catalog.controller;

import com.uagrm.schedule_assignment.academic_catalog.dto.AcademicPeriodRequestDto;
import com.uagrm.schedule_assignment.academic_catalog.dto.AcademicPeriodResponseDto;
import com.uagrm.schedule_assignment.academic_catalog.service.AcademicPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/academic-periods")
@RequiredArgsConstructor
@Tag(name = "Academic Period", description = "Endpoints para la gestión de periodos académicos")
public class AcademicPeriodController {

    private final AcademicPeriodService academicPeriodService;

    @PostMapping
    @Operation(summary = "Crear un nuevo periodo académico")
    public ResponseEntity<AcademicPeriodResponseDto> createAcademicPeriod(@RequestBody AcademicPeriodRequestDto requestDto) {
        return new ResponseEntity<>(academicPeriodService.createAcademicPeriod(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los periodos académicos")
    public ResponseEntity<Iterable<AcademicPeriodResponseDto>> getAllAcademicPeriods() {
        return ResponseEntity.ok(academicPeriodService.getAllAcademicPeriods());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un periodo académico por ID")
    public ResponseEntity<AcademicPeriodResponseDto> getAcademicPeriodById(@PathVariable Long id) {
        return ResponseEntity.ok(academicPeriodService.getAcademicPeriodById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un periodo académico existente")
    public ResponseEntity<AcademicPeriodResponseDto> updateAcademicPeriod(@PathVariable Long id, @RequestBody AcademicPeriodRequestDto requestDto) {
        return ResponseEntity.ok(academicPeriodService.updateAcademicPeriod(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un periodo académico")
    public ResponseEntity<Void> deleteAcademicPeriod(@PathVariable Long id) {
        academicPeriodService.deleteAcademicPeriod(id);
        return ResponseEntity.noContent().build();
    }
}