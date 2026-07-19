package com.uagrm.personal.workshop.dto;

import java.time.LocalDate;

public record FrameDeliveryResponseDto(
        Long id,
        Long orderId,
        LocalDate deliveryDate,
        Integer quantityDelivered,
        String remarks
) {
}
