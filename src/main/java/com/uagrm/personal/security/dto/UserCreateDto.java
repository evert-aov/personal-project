package com.uagrm.personal.security.dto;

import java.util.Set;

public record UserCreateDto(
        String username,
        String name,
        String lastName,
        String email,
        String password,
        Set<Long> rolesIds
) {
}
