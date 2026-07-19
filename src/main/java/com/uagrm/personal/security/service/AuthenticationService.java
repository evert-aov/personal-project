package com.uagrm.personal.security.service;

import com.uagrm.personal.security.dto.AuthResponseDto;
import com.uagrm.personal.security.dto.LoginRequestDto;
import com.uagrm.personal.security.mapper.AuthResponseMapper;
import com.uagrm.personal.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de la autenticación de usuarios y gestión de tokens JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthResponseMapper authResponseMapper;

    /**
     * Autentica a un usuario y genera un par de tokens (Acceso y Refresh).
     *
     * @param request Datos de inicio de sesión (usuario y contraseña).
     * @return AuthResponseDto conteniendo el token de acceso y el refresh token.
     * @throws UsernameNotFoundException Si el usuario no existe.
     */
    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        var tokenRefresh = jwtService.generateRefreshToken(user);

        return authResponseMapper.toDto(jwtToken, tokenRefresh);
    }

    /**
     * Genera un nuevo token de acceso a partir de un refresh token válido.
     *
     * @param refreshToken El token de actualización proporcionado por el usuario.
     * @return AuthResponseDto con el nuevo token de acceso y el mismo refresh token.
     * @throws RuntimeException Si el usuario no es encontrado o el token es inválido.
     */
    public AuthResponseDto refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        var newToken = jwtService.generateToken(user);

        return authResponseMapper.toDto(newToken, refreshToken);
    }
}
