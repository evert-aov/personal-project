package com.uagrm.schedule_assignment.security.dto;

import java.util.Set;

public record UserCreateDto(
        String name,
        String lastName,
        String email,
        String password,
        Set<Long> rolesIds
) {
}
