package com.meetime.desafio.infrastructure.web.controller;

import com.meetime.desafio.domain.port.in.OAuthUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthUseCase oAuthUseCase;

    @GetMapping("/authorize")
    public ResponseEntity<String> authorize() {
        String redirectUrl = oAuthUseCase.generateAuthorizationUrl();

        return ResponseEntity.ok(redirectUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam @NonNull String code) {
        try {
            String token = oAuthUseCase.handleCallback(code);
            return ResponseEntity.ok(token);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Erro ao processar o token: " + e.getMessage());
        }
    }
}

