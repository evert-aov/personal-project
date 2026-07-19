package com.uagrm.personal.workshop.dto;

import java.math.BigDecimal;

public record PictureFrameRequestDto(
        BigDecimal widthCm,
        BigDecimal heightCm,
        String dimensionDescription,
        BigDecimal price,
        boolean active
) {
}
