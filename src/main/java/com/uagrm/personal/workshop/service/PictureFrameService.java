package com.uagrm.personal.workshop.service;

import com.uagrm.personal.workshop.dto.PictureFrameRequestDto;
import com.uagrm.personal.workshop.dto.PictureFrameResponseDto;
import com.uagrm.personal.workshop.entity.PictureFrame;
import com.uagrm.personal.workshop.mapper.PictureFrameMapper;
import com.uagrm.personal.workshop.repository.PictureFrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PictureFrameService {
    private final PictureFrameRepository pictureFrameRepository;
    private final PictureFrameMapper pictureFrameMapper;

    @Transactional
    public PictureFrameResponseDto createFrame(PictureFrameRequestDto requestDto) {
        return pictureFrameMapper.toDto(pictureFrameRepository.save(pictureFrameMapper.toEntity(requestDto)));
    }

    @Transactional(readOnly = true)
    public Iterable<PictureFrameResponseDto> getAllFrames() {
        return pictureFrameRepository.findAll().stream().map(pictureFrameMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PictureFrameResponseDto getFrameById(Long id) {
        return pictureFrameMapper.toDto(findFrame(id));
    }

    @Transactional
    public PictureFrameResponseDto updateFrame(Long id, PictureFrameRequestDto requestDto) {
        PictureFrame frame = findFrame(id);
        frame.setWidthCm(requestDto.widthCm());
        frame.setHeightCm(requestDto.heightCm());
        frame.setDimensionDescription(requestDto.dimensionDescription());
        frame.setPrice(requestDto.price());
        frame.setActive(requestDto.active());
        return pictureFrameMapper.toDto(pictureFrameRepository.save(frame));
    }

    @Transactional
    public void deleteFrame(Long id) {
        pictureFrameRepository.deleteById(id);
    }

    PictureFrame findFrame(Long id) {
        return pictureFrameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Picture frame not found"));
    }
}
