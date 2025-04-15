package com.meetime.desafio.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "hubspotWebClient")
    public WebClient hubspotWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.hubapi.com")
                .build();
    }
}