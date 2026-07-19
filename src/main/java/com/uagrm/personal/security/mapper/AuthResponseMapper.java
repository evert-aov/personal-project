package com.uagrm.personal.security.mapper;

import com.uagrm.personal.security.dto.AuthResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseMapper {
    public AuthResponseDto toDto(String token, String refreshToken) {
        return new AuthResponseDto(token, refreshToken);
    }
}
