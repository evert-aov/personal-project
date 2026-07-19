package com.uagrm.personal.security.dto;

import java.math.BigDecimal;
import java.util.Set;

public record UserResponseDto(
        Long id,
        String username,
        String name,
        String lastName,
        String email,
        Boolean isActive,
        Set<RoleResponseDto> roles,
        BigDecimal advanceBalance
) {
}
