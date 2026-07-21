package com.uagrm.personal.config;

import com.uagrm.personal.security.config.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Value("${cors.allowed-origins:http://localhost:4200,http://192.168.100.125:4200,http://127.0.0.1:4200,https://*.vercel.app,https://persnal-project-front*.vercel.app}")
    private String allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        configuration.setAllowedOriginPatterns(origins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Tenant-ID", "Cache-Control"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "X-Tenant-ID"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Public endpoints
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/security/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(POST, "/api/security/users").permitAll()

                        // OnlyOffice Document Server callbacks
                        .requestMatchers(HttpMethod.GET, "/api/notes/*/file").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/notes/*/callback").permitAll()

                        // Dashboard público accesible para todos
                        .requestMatchers("/api/dashboard/**").permitAll()

                        // Módulos note y academic: SOLO para SUPERUSER
                        .requestMatchers(
                                "/api/academic-periods/**",
                                "/api/classrooms/**",
                                "/api/schedules/**",
                                "/api/subjects/**",
                                "/api/groups/**",
                                "/api/notes/**",
                                "/api/whiteboards/**"
                        ).hasAnyAuthority("SUPERUSER", "superuser", "ROLE_SUPERUSER")

                        // Módulo finance - Endpoints administrativos: ADMIN y SUPERUSER
                        .requestMatchers("/api/finance/admin/**")
                                .hasAnyAuthority("ADMIN", "admin", "ROLE_ADMIN", "SUPERUSER", "superuser", "ROLE_SUPERUSER")

                        // Módulo finance - Endpoints de cliente: USER, ADMIN y SUPERUSER
                        .requestMatchers(
                                "/api/finance/client/**",
                                "/api/finance/transactions/**"
                        ).hasAnyAuthority("USER", "user", "ROLE_USER", "ADMIN", "admin", "ROLE_ADMIN", "SUPERUSER", "superuser", "ROLE_SUPERUSER")

                        // Módulo workshop - Parte pública / cliente (GET): USER, ADMIN y SUPERUSER
                        .requestMatchers("/api/workshop/**")
                                .hasAnyAuthority("USER", "user", "ROLE_USER", "ADMIN", "admin", "ROLE_ADMIN", "SUPERUSER", "superuser", "ROLE_SUPERUSER")

                        // Módulo workshop - Endpoints completos (escritura): ADMIN y SUPERUSER
                        .requestMatchers("/api/workshop/**")
                                .hasAnyAuthority("ADMIN", "admin", "ROLE_ADMIN", "SUPERUSER", "superuser", "ROLE_SUPERUSER")

                        // Perfil de usuario y otros endpoints autenticados
                        .requestMatchers("/api/profile/**").authenticated()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
