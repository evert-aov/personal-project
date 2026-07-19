package com.uagrm.personal.security.mapper;

import com.uagrm.personal.security.dto.RoleResponseDto;
import com.uagrm.personal.security.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleResponseDto toDto(Role role) {
        return new RoleResponseDto(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getLevel(),
                role.getIsActive()
        );
    }
}
