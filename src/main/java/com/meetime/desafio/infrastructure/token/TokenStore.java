package com.meetime.desafio.infrastructure.token;

import com.meetime.desafio.domain.port.out.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class TokenStore implements TokenRepository {

    private static final String ACCESS_TOKEN_KEY = "hubspot:access_token";
    private static final String REFRESH_TOKEN_KEY = "hubspot:refresh_token";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveAccessToken(String token, long expiresInSeconds) {
        redisTemplate.opsForValue().set(ACCESS_TOKEN_KEY, token, Duration.ofSeconds(expiresInSeconds));
    }

    @Override
    public String getAccessToken() {
        return redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY);
    }

    @Override
    public boolean hasToken() {
        return redisTemplate.hasKey(ACCESS_TOKEN_KEY);
    }

    @Override
    public void saveRefreshToken(String token) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY, token);
    }

    @Override
    public String getRefreshToken() {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY);
    }

    @Override
    public void saveOAuthState(String state) {
        redisTemplate.opsForValue().set("hubspot:oauth_state:" + state, "valid", Duration.ofMinutes(5));
    }

    @Override
    public boolean isValidState(String state) {
        return redisTemplate.hasKey("hubspot:oauth_state:" + state);
    }

    @Override
    public void deleteOAuthState(String state) {
        redisTemplate.delete("hubspot:oauth_state:" + state);
    }
}
