package com.uagrm.schedule_assignment.security.dto;

import java.util.Set;

public record UserResponseDto(
        Long id,
        Integer code,
        String name,
        String lastName,
        String email,
        Boolean isActive,
        Set<RoleResponseDto> roles
) {
}
