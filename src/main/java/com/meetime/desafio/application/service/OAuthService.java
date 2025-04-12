package com.meetime.desafio.application.service;

import com.meetime.desafio.domain.port.in.OAuthUseCase;
import com.meetime.desafio.infrastructure.config.HubspotOAuthProperties;
import com.meetime.desafio.infrastructure.hubspot.client.HubspotOAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService implements OAuthUseCase {

    private final HubspotOAuthProperties properties;
    private final HubspotOAuthClient hubspotOAuthClient;

    @Override
    public String generateAuthorizationUrl() {
        return properties.getAuthUrl() +
                "?client_id=" + properties.getClientId() +
                "&redirect_uri=" + properties.getRedirectUri() +
                "&scope=contacts" +
                "&response_type=code";
    }

    @Override
    public void handleCallback(String code) {
        hubspotOAuthClient.exchangeCodeForToken(code);
    }
}
