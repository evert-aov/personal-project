package com.uagrm.schedule_assignment.security.dto;

import java.util.Set;

public record UserUpdateDto(
        String name,
        String lastName,
        String password,
        Boolean isActive,
        Set<Long> rolesIds
) {
}
