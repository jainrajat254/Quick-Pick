package com.rajat.quickpick.security;


import com.rajat.quickpick.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/auth/register/user",
            "/api/auth/register/vendor",
            "/api/auth/login/user",
            "/api/auth/login/vendor",
            "/api/auth/verify-email",
            "/api/auth/forgot-password",
            "/api/auth/reset-password",
            "/api/auth/resend-verification",
            "/api/auth/refresh-token"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        if (path.startsWith("/ws/")) return true;
        if (path.equals("/api/admin/create") || path.equals("/api/admin/login")) return true;
        return PUBLIC_PATHS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        log.debug("Processing request to: {}", request.getRequestURI());
        log.debug("Authorization header present: {}", authorizationHeader != null);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            log.debug("JWT token extracted, length: {}", jwt.length());
            try {
                username = jwtUtil.extractUsername(jwt);
                log.debug("Username extracted from token: {}", username);
            } catch (Exception e) {
                log.error("Error extracting username from JWT token: {}", e.getMessage());
                log.error("Token validation failed with exception: ", e);
            }
        } else {
            log.warn("No valid Authorization header found. Header value: {}", authorizationHeader);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("Attempting to authenticate user: {}", username);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                log.debug("JWT token is valid for user: {}", username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Authentication set successfully for user: {} with authorities: {}", username, userDetails.getAuthorities());
            } else {
                log.error("JWT token validation failed for user: {}", username);
            }
        } else if (username == null) {
            log.warn("Username is null, cannot authenticate");
        } else {
            log.debug("Authentication already exists in SecurityContext");
        }

        filterChain.doFilter(request, response);
    }
}