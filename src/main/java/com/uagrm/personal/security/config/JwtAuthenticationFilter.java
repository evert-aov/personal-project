package com.uagrm.personal.security.config;

import com.uagrm.personal.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Public endpoints called by external services (OnlyOffice Document Server) that
    // sign their own Authorization: Bearer token with a different scheme -- this filter
    // must not try to validate it as one of our own app JWTs.
    private static final Pattern EXTERNALLY_SIGNED_PATHS = Pattern.compile("^/api/(notes|whiteboards)/[^/]+/(file|callback)$");

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Skip filter if it's an auth endpoint
        if (request.getServletPath().contains("/api/security/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Skip filter for endpoints external services call with their own signed token
        // (e.g. OnlyOffice Document Server hitting /api/notes/{id}/file|callback).
        if (EXTERNALLY_SIGNED_PATHS.matcher(request.getServletPath()).matches()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        // A Bearer token that isn't one of ours (wrong signature, malformed, expired, or
        // naming a user that no longer exists) must not blow up the whole request with a
        // 500 -- it should just fail to authenticate and let the authorization rules decide.
        try {
            username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.debug("Ignoring unparseable/foreign Bearer token on {}: {}", request.getServletPath(), e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
