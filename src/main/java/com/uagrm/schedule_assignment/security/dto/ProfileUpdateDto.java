package com.uagrm.schedule_assignment.security.dto;

public record ProfileUpdateDto(
        String name,
        String lastName,
        String password
) {
}
