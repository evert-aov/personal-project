package com.uagrm.personal.security.dto;

public record RoleResponseDto(
        Long id,
        String name,
        String description,
        Integer level,
        Boolean isActive
) {
}
