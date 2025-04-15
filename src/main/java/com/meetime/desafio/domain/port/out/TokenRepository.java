package com.meetime.desafio.domain.port.out;

public interface TokenRepository {
    void saveAccessToken(String token);
    String getAccessToken();
    boolean hasToken();
}
