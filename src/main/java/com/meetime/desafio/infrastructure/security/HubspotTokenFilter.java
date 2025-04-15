package com.meetime.desafio.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class HubspotTokenFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final HubspotTokenValidator tokenValidator;

    public HubspotTokenFilter(HubspotTokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info(">> HubspotTokenFilter executado para {}", request.getRequestURI());

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Objects.isNull(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Token não informado ou inválido.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token ausente ou inválido");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        log.info("Token em Substring {}", token);

        Boolean isValid = tokenValidator.isTokenValid(token).block();
        log.info("Status do Token: {}", isValid);

        if (Boolean.FALSE.equals(isValid)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token inválido ou expirado");
            return;
        }

        Authentication auth = new UsernamePasswordAuthenticationToken("hubspotUser", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        log.info("Token aceito, seguindo requisição...");
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/oauth");
    }
}
