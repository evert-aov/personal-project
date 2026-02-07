package com.uagrm.schedule_assignment.security.mapper;

import com.uagrm.schedule_assignment.security.dto.AuthResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseMapper {
    public AuthResponseDto toDto(String token, String refreshToken) {
        return new AuthResponseDto(token, refreshToken);
    }
}
