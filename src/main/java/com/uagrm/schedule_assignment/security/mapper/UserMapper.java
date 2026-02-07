package com.uagrm.schedule_assignment.security.mapper;

import com.uagrm.schedule_assignment.security.dto.UserResponseDto;
import com.uagrm.schedule_assignment.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getCode(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getIsActive(),
                user.getRoles()
                        .stream()
                        .map(roleMapper::toDto)
                        .collect(Collectors.toSet())
        );
    }
}
