package com.uagrm.personal.academic_catalog.service;

import com.uagrm.personal.academic_catalog.dto.AcademicPeriodRequestDto;
import com.uagrm.personal.academic_catalog.dto.AcademicPeriodResponseDto;
import com.uagrm.personal.academic_catalog.entity.AcademicPeriod;
import com.uagrm.personal.academic_catalog.mapper.AcademicMapper;
import com.uagrm.personal.academic_catalog.repository.AcademicPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para la gestión de periodos académicos.
 * Proporciona métodos para crear, leer, actualizar y eliminar periodos.
 */
@Service
@RequiredArgsConstructor
public class AcademicPeriodService {
    private final AcademicPeriodRepository academicRepository;
    private final AcademicMapper academicMapper;

    /**
     * Crea un nuevo periodo académico.
     * @param requestDto Datos del periodo a crear.
     * @return El periodo académico creado en formato DTO.
     */
    @Transactional
    public AcademicPeriodResponseDto createAcademicPeriod(AcademicPeriodRequestDto requestDto) {
        return academicMapper.toDto(academicRepository.save(academicMapper.toEntity(requestDto)));
    }

    /**
     * Obtiene todos los periodos académicos registrados.
     * @return Colección de periodos académicos.
     */
    @Transactional(readOnly = true)
    public Iterable<AcademicPeriodResponseDto> getAllAcademicPeriods() {
        return academicRepository.findAll()
                .stream()
                .map(academicMapper::toDto)
                .toList();
    }

    /**
     * Obtiene un periodo académico específico por su ID.
     * @param id Identificador único del periodo.
     * @return El periodo académico encontrado.
     * @throws RuntimeException si el periodo no existe.
     */
    @Transactional(readOnly = true)
    public AcademicPeriodResponseDto getAcademicPeriodById(Long id) {
        return academicMapper.toDto(academicRepository.findById(id).orElseThrow(() -> new RuntimeException("Academic period not found")));
    }

    @Transactional
    public AcademicPeriodResponseDto updateAcademicPeriod(Long id, @NonNull AcademicPeriodRequestDto requestDto) {
        AcademicPeriod academicPeriod = academicRepository.findById(id).orElseThrow(() -> new RuntimeException("Academic period not found"));
        academicPeriod.setName(requestDto.name());
        academicPeriod.setStartDate(requestDto.startDate());
        academicPeriod.setEndDate(requestDto.endDate());
        return academicMapper.toDto(academicRepository.save(academicPeriod));
    }

    @Transactional
    public void deleteAcademicPeriod(Long id) {
        academicRepository.deleteById(id);
    }
}
