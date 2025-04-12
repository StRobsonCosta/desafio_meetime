package com.meetime.desafio.infrastructure.web.controller;

import com.meetime.desafio.domain.port.in.OAuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthUseCase oAuthUseCase;

    @GetMapping("/authorize")
    public String authorize() {
        return oAuthUseCase.generateAuthorizationUrl();
    }

    @GetMapping("/callback")
    public void callback(@RequestParam String code) {
        oAuthUseCase.handleCallback(code);
    }
}

