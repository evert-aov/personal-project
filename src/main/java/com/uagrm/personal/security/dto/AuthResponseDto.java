package com.uagrm.personal.security.dto;

public record
AuthResponseDto(
        String token,
        String refreshToken
) {
}
