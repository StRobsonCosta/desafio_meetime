package com.meetime.desafio.domain.port.out;

public interface TokenRepository {
    void saveAccessToken(String token, long expiresInSeconds);
    String getAccessToken();
    boolean hasToken();
    void saveRefreshToken(String token);
    String getRefreshToken();

    void saveOAuthState(String state);
    boolean isValidState(String state);
    void deleteOAuthState(String state);
}
