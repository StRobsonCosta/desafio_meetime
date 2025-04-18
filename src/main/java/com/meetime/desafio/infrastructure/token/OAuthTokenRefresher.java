package com.meetime.desafio.infrastructure.token;

import com.meetime.desafio.application.dto.OAuthTokenResponse;
import com.meetime.desafio.domain.port.out.TokenRepository;
import com.meetime.desafio.infrastructure.hubspot.client.HubspotOAuthClient;
import com.meetime.desafio.shared.exception.TokenExchangeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthTokenRefresher {

    private final TokenRepository tokenStore;
    private final HubspotOAuthClient hubspotOAuthClient;

    // Executa a cada 5 minutos
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void refreshTokenIfNeeded() {
        log.info("Verificando necessidade de renovar o token...");

        if (!tokenStore.hasToken()) {
            String refreshToken = tokenStore.getRefreshToken();
            if (refreshToken != null) {
                try {
                    OAuthTokenResponse response = hubspotOAuthClient.exchangeCodeForToken(refreshToken);
                    tokenStore.saveAccessToken(response.getAccessToken(), response.getExpiresIn());

                    log.info("Token renovado com sucesso!");
                } catch (Exception e) {
                    log.error("Erro ao renovar o token de acesso", e);
                    throw new TokenExchangeException("Erro ao renovar o token de acesso", e);
                }
            } else {
                log.warn("Nenhum refresh token disponível para renovação.");
            }
        } else {
            log.info("Token de acesso ainda válido.");
        }
    }

}
