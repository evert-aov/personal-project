package com.uagrm.personal.security.mapper;

import com.uagrm.personal.security.dto.UserResponseDto;
import com.uagrm.personal.security.entity.User;
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
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getIsActive(),
                user.getRoles()
                        .stream()
                        .map(roleMapper::toDto)
                        .collect(Collectors.toSet()),
                user.getAdvanceBalance()
        );
    }
}
