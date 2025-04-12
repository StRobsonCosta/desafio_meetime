package com.meetime.desafio.domain.port.in;

public interface OAuthUseCase {

    String generateAuthorizationUrl();
    void handleCallback(String code);
}
