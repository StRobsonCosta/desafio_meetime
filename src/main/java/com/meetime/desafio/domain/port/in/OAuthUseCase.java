package com.meetime.desafio.domain.port.in;

public interface OAuthUseCase {

    String generateAuthorizationUrl();
    String handleCallback(String code, String state);
}
