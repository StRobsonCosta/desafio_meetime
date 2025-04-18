package com.meetime.desafio.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final HubspotOAuthProperties properties;

    @Bean(name = "hubspotWebClient")
    public WebClient hubspotWebClient() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }
}