package com.uagrm.schedule_assignment.security.dto;

public record AuthResponseDto(
        String token,
        String refreshToken
) {
}
