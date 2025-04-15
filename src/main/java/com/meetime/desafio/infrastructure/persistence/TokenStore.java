package com.meetime.desafio.infrastructure.persistence;

import com.meetime.desafio.domain.port.out.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenStore implements TokenRepository {

    private static final String ACCESS_TOKEN_KEY = "hubspot:access_token";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveAccessToken(String token) {
        redisTemplate.opsForValue().set(ACCESS_TOKEN_KEY, token);
    }

    @Override
    public String getAccessToken() {
        return redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY);
    }

    @Override
    public boolean hasToken() {
        return Boolean.TRUE.equals(redisTemplate.hasKey(ACCESS_TOKEN_KEY));
    }
}
