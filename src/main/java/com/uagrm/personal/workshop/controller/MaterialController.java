package com.uagrm.personal.workshop.controller;

import com.uagrm.personal.workshop.dto.MaterialRequestDto;
import com.uagrm.personal.workshop.dto.MaterialResponseDto;
import com.uagrm.personal.workshop.service.MaterialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workshop/materials")
@RequiredArgsConstructor
@Tag(name = "Workshop - Materials", description = "Materiales e inventario (materiales)")
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<MaterialResponseDto> createMaterial(@RequestBody MaterialRequestDto requestDto) {
        return new ResponseEntity<>(materialService.createMaterial(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<MaterialResponseDto>> getAllMaterials() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDto> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDto> updateMaterial(@PathVariable Long id, @RequestBody MaterialRequestDto requestDto) {
        return ResponseEntity.ok(materialService.updateMaterial(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
