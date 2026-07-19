package com.uagrm.personal.workshop.mapper;

import com.uagrm.personal.workshop.dto.FrameDeliveryResponseDto;
import com.uagrm.personal.workshop.entity.FrameDelivery;
import org.springframework.stereotype.Component;

@Component
public class FrameDeliveryMapper {
    public FrameDeliveryResponseDto toDto(FrameDelivery entity) {
        return new FrameDeliveryResponseDto(
                entity.getId(),
                entity.getOrder().getId(),
                entity.getDeliveryDate(),
                entity.getQuantityDelivered(),
                entity.getRemarks()
        );
    }
}
