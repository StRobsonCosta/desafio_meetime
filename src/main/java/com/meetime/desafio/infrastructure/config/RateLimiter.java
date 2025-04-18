package com.meetime.desafio.infrastructure.config;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RateLimiter {

    private final RateLimiterRegistry registry;

    public RateLimiter() {
        this.registry = RateLimiterRegistry.ofDefaults();
    }

    public boolean tryAcquire() {
        io.github.resilience4j.ratelimiter.RateLimiter limiter = registry.rateLimiter("hubspot");
        return limiter.acquirePermission();
    }
}
