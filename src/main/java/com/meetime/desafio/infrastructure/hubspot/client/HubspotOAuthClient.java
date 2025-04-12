package com.meetime.desafio.infrastructure.hubspot.client;

import com.meetime.desafio.infrastructure.config.HubspotOAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class HubspotOAuthClient {

    private final WebClient webClient;
    private final HubspotOAuthProperties properties;

    public void exchangeCodeForToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        form.add("redirect_uri", properties.getRedirectUri());
        form.add("code", code);

        webClient.post()
                .uri(properties.getTokenUrl())
                .bodyValue(form)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
