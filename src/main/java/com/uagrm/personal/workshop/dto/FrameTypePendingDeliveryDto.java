package com.uagrm.personal.workshop.dto;

public record FrameTypePendingDeliveryDto(
        Long frameId,
        String frameDescription,
        Integer totalRequested,
        Integer totalDelivered,
        Integer totalPending
) {
}
