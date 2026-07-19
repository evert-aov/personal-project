package com.uagrm.personal.workshop.mapper;

import com.uagrm.personal.workshop.dto.PictureFrameRequestDto;
import com.uagrm.personal.workshop.dto.PictureFrameResponseDto;
import com.uagrm.personal.workshop.entity.PictureFrame;
import org.springframework.stereotype.Component;

@Component
public class PictureFrameMapper {
    public PictureFrame toEntity(PictureFrameRequestDto dto) {
        if (dto == null) return null;

        return PictureFrame.builder()
                .widthCm(dto.widthCm())
                .heightCm(dto.heightCm())
                .dimensionDescription(dto.dimensionDescription())
                .price(dto.price())
                .active(dto.active())
                .build();
    }

    public PictureFrameResponseDto toDto(PictureFrame entity) {
        return new PictureFrameResponseDto(
                entity.getId(),
                entity.getWidthCm(),
                entity.getHeightCm(),
                entity.getDimensionDescription(),
                entity.getPrice(),
                entity.isActive()
        );
    }
}
