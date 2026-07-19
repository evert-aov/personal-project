package com.uagrm.personal.workshop.dto;

public record OrderPendingDeliveryDto(
        Long orderId,
        Long frameId,
        String frameDescription,
        Integer requestedQuantity,
        Integer deliveredQuantity,
        Integer pendingQuantity
) {
}
