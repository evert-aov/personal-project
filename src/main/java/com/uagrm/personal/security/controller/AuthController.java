package com.uagrm.personal.security.controller;

import com.uagrm.personal.security.dto.AuthResponseDto;
import com.uagrm.personal.security.dto.LoginRequestDto;
import com.uagrm.personal.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/security/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para autenticación y gestión de tokens")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica a un usuario con su usuario y contraseña, devolviendo un token JWT.")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.login(loginRequestDto));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refrescar token", description = "Genera un nuevo token de acceso a partir de un refresh token válido.")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.substring(7);
        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }

    /**
     * authenticationManager.authenticate() is called directly from AuthenticationService
     * (not from the security filter chain), so its AuthenticationException never goes through
     * Spring Security's normal 401 translation. Without this handler it bubbles up as a bare 500.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Usuario o contraseña incorrectos"));
    }
}
