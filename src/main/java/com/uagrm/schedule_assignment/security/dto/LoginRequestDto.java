package com.uagrm.schedule_assignment.security.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LoginRequestDto(
        @NotNull(message = "Code is required")
        @Positive(message = "Code must be positive")
        @Min(1000)
        Integer code,

        @NotBlank(message = "Password is required")
        String password
) {
}
