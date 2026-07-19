package com.uagrm.personal.security.dto;

public record ProfileUpdateDto(
        String name,
        String lastName,
        String password
) {
}
