package com.uagrm.schedule_assignment.security.service;

import com.uagrm.schedule_assignment.security.dto.AuthResponseDto;
import com.uagrm.schedule_assignment.security.dto.LoginRequestDto;
import com.uagrm.schedule_assignment.security.mapper.AuthResponseMapper;
import com.uagrm.schedule_assignment.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthResponseMapper authResponseMapper;

    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.code().toString(),
                        request.password()
                )
        );
        var user = userRepository.findByCode(request.code())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        var tokenRefresh = jwtService.generateRefreshToken(user);

        return authResponseMapper.toDto(jwtToken, tokenRefresh);
    }

    public AuthResponseDto refreshToken(String refreshToken) {
        Integer username = Integer.valueOf(jwtService.extractUsername(refreshToken));
        var user = userRepository.findByCode(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        var newToken = jwtService.generateToken(user);

        return authResponseMapper.toDto(newToken, refreshToken);
    }
}
