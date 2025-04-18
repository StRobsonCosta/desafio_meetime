package com.meetime.desafio.application.service;

import com.meetime.desafio.application.dto.OAuthTokenResponse;
import com.meetime.desafio.domain.port.in.OAuthUseCase;
import com.meetime.desafio.domain.port.out.TokenRepository;
import com.meetime.desafio.infrastructure.config.HubspotOAuthProperties;
import com.meetime.desafio.infrastructure.hubspot.client.HubspotOAuthClient;
import com.meetime.desafio.shared.exception.InvalidOAuthStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService implements OAuthUseCase {

    private final HubspotOAuthProperties properties;
    private final HubspotOAuthClient hubspotOAuthClient;
    private final TokenRepository tokenStore;

    @Override
    public String generateAuthorizationUrl() {
        log.info("Obtendo URL para validação e gerar Code de CallBack");

        String state = UUID.randomUUID().toString();
        tokenStore.saveOAuthState(state);

        return properties.getAuthUrl() +
                "?client_id=" + properties.getClientId() +
                "&redirect_uri=" + properties.getRedirectUri() +
                "&scope=" + properties.getClientScopes() +
                "&response_type=code";
    }

    @Override
    public String handleCallback(String code, String state) {
        log.info("Iniciando HandleCallBack");

        if (!tokenStore.isValidState(state))
            throw new InvalidOAuthStateException("State inválido ou expirado.");

        OAuthTokenResponse response = hubspotOAuthClient.exchangeCodeForToken(code);
        tokenStore.saveAccessToken(response.getAccessToken(), response.getExpiresIn());
        tokenStore.saveRefreshToken(response.getRefreshToken());

        tokenStore.deleteOAuthState(state);

        log.info("Token Salvo");
        return response.getAccessToken();
    }
}
