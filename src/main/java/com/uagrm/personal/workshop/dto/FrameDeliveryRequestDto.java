package com.uagrm.personal.workshop.dto;

import java.time.LocalDate;

public record FrameDeliveryRequestDto(
        LocalDate deliveryDate,
        Integer quantityDelivered,
        String remarks
) {
}
