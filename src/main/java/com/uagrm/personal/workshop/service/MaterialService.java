package com.uagrm.personal.workshop.service;

import com.uagrm.personal.workshop.dto.MaterialRequestDto;
import com.uagrm.personal.workshop.dto.MaterialResponseDto;
import com.uagrm.personal.workshop.entity.Material;
import com.uagrm.personal.workshop.exception.InsufficientStockException;
import com.uagrm.personal.workshop.mapper.MaterialMapper;
import com.uagrm.personal.workshop.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    @Transactional
    public MaterialResponseDto createMaterial(MaterialRequestDto requestDto) {
        return materialMapper.toDto(materialRepository.save(materialMapper.toEntity(requestDto)));
    }

    @Transactional(readOnly = true)
    public Iterable<MaterialResponseDto> getAllMaterials() {
        return materialRepository.findAll().stream().map(materialMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public MaterialResponseDto getMaterialById(Long id) {
        return materialMapper.toDto(findMaterial(id));
    }

    @Transactional
    public MaterialResponseDto updateMaterial(Long id, MaterialRequestDto requestDto) {
        Material material = findMaterial(id);
        material.setName(requestDto.name());
        material.setType(requestDto.type());
        material.setDimensions(requestDto.dimensions());
        material.setUnitOfMeasure(requestDto.unitOfMeasure());
        material.setMinStock(requestDto.minStock());
        if (requestDto.unitPrice() != null) {
            material.setUnitPrice(requestDto.unitPrice());
        }
        return materialMapper.toDto(materialRepository.save(material));
    }

    @Transactional
    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }

    /**
     * Replaces fn_stock_add_purchase: called by PurchaseService when a purchase detail is registered.
     */
    @Transactional
    public void addStock(Long materialId, BigDecimal quantity) {
        Material material = findMaterialForUpdate(materialId);
        material.setCurrentStock(material.getCurrentStock().add(quantity));
        materialRepository.save(material);
    }

    /**
     * Replaces fn_stock_subtract_usage: validates availability before subtracting,
     * raising a business exception instead of letting the database fail silently.
     */
    @Transactional
    public void subtractStock(Long materialId, BigDecimal quantity) {
        Material material = findMaterialForUpdate(materialId);
        if (material.getCurrentStock().compareTo(quantity) < 0) {
            throw new InsufficientStockException(materialId, material.getCurrentStock(), quantity);
        }
        material.setCurrentStock(material.getCurrentStock().subtract(quantity));
        materialRepository.save(material);
    }

    Material findMaterial(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));
    }

    private Material findMaterialForUpdate(Long id) {
        return materialRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));
    }
}
