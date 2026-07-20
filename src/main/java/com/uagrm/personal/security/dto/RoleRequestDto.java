package com.uagrm.personal.security.dto;

public record RoleRequestDto(
        String name,
        String description,
        Integer level,
        Boolean isActive
) {
}
