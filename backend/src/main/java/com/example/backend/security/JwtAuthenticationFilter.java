package com.example.backend.security;

import com.example.backend.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            logger.info("JWT Filter - Request URI: " + request.getRequestURI());
            logger.info("JWT Filter - JWT Token: "
                    + (jwt != null ? jwt.substring(0, Math.min(20, jwt.length())) + "..." : "null"));

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Claims claims = tokenProvider.getClaimsFromJWT(jwt);
                String username = claims.getSubject();
                List<String> authoritiesList = claims.get("auth", List.class);
                if (authoritiesList == null) {
                    authoritiesList = claims.get("authorities", List.class);
                }

                // Xử lý trường hợp authoritiesList là null
                List<SimpleGrantedAuthority> authorities = null;
                if (authoritiesList != null && !authoritiesList.isEmpty()) {
                    authorities = authoritiesList.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                } else {
                    // Nếu không có authorities trong token, tạo default authority
                    authorities = Arrays.asList(new SimpleGrantedAuthority("USER"));
                }

                logger.info("JWT Filter - Username: " + username);
                logger.info("JWT Filter - Authorities: " + authorities);

                if (authorities != null && !authorities.isEmpty()) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("JWT Filter - Authentication set successfully for user: " + username);
                } else {
                    logger.warn("JWT Filter - No authorities found in token for user: " + username);
                }
            } else {
                logger.info("JWT Filter - No valid JWT token found");
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}