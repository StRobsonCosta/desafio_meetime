package com.meetime.desafio.infrastructure.hubspot.client;

import com.meetime.desafio.application.dto.OAuthTokenResponse;
import com.meetime.desafio.infrastructure.config.HubspotOAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubspotOAuthClient {

    private final WebClient webClient;
    private final HubspotOAuthProperties properties;

    public String exchangeCodeForToken(String code) {

        log.info("Enviando request para {}", properties.getTokenUrl());
        log.info("redirect_uri: {}", properties.getRedirectUri());
        log.info("code: {}", code);


        log.info("Iniciando o método exchangeCodeForToken");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        form.add("redirect_uri", properties.getRedirectUri());
        form.add("code", code);

        OAuthTokenResponse response = webClient.post()
                .uri(properties.getTokenUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        respons -> respons.bodyToMono(String.class)
                                .map(body -> {
                                    log.error("Erro ao criar contato: {}", body);
                                    return new RuntimeException("Erro ao chamar HubSpot: " + body);
                                })
                )
                .bodyToMono(OAuthTokenResponse.class)
                .block();

        if (Objects.isNull(response) || Objects.isNull(response.getAccessToken()))
            throw new IllegalStateException("Não foi possível obter o token de acesso do HubSpot");

        log.info("Retornando Token {}", response.getAccessToken());
        log.info("Expiration Time {}", response.getExpiresIn());

        return response.getAccessToken();
    }
}
