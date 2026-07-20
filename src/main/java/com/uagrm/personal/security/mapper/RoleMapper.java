package com.uagrm.personal.security.mapper;

import com.uagrm.personal.security.dto.RoleRequestDto;
import com.uagrm.personal.security.dto.RoleResponseDto;
import com.uagrm.personal.security.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleResponseDto toDto(Role role) {
        if (role == null) return null;
        return new RoleResponseDto(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getLevel(),
                role.getIsActive()
        );
    }

    public Role toEntity(RoleRequestDto dto) {
        if (dto == null) return null;
        return Role.builder()
                .name(dto.name())
                .description(dto.description())
                .level(dto.level() != null ? dto.level() : 1)
                .isActive(dto.isActive() != null ? dto.isActive() : true)
                .build();
    }
}
