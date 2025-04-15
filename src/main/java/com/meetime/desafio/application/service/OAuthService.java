package com.meetime.desafio.application.service;

import com.meetime.desafio.domain.port.in.OAuthUseCase;
import com.meetime.desafio.infrastructure.config.HubspotOAuthProperties;
import com.meetime.desafio.infrastructure.hubspot.client.HubspotOAuthClient;
import com.meetime.desafio.infrastructure.persistence.TokenStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService implements OAuthUseCase {

    private final HubspotOAuthProperties properties;
    private final HubspotOAuthClient hubspotOAuthClient;
    private final TokenStore tokenStore;

    @Override
    public String generateAuthorizationUrl() {
        log.info("Obtendo URL para validação e gerar Code de CallBack");

        return properties.getAuthUrl() +
                "?client_id=" + properties.getClientId() +
                "&redirect_uri=" + properties.getRedirectUri() +
                "&scope=crm.objects.contacts.read%20crm.objects.contacts.write" +
                "&response_type=code";
    }

    @Override
    public String handleCallback(String code) {
        log.info("Iniciando HandleCallBack");

        String accessToken = hubspotOAuthClient.exchangeCodeForToken(code);
        tokenStore.saveAccessToken(accessToken);

        log.info("Token Salvo");

        return accessToken;
    }
}
