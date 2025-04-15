package com.meetime.desafio.infrastructure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class HubspotTokenValidator {

    private final WebClient webClient;

    public HubspotTokenValidator(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.hubapi.com").build();
    }

    public Mono<Boolean> isTokenValid(String token) {
        log.info("Validando a integridade do Token");
        return webClient
                .get()
                .uri("/oauth/v1/access-tokens/{token}", token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("Erro no onStatus");
                    return Mono.just(new RuntimeException("Erro ao chamar HubSpot: "));
                })
                .bodyToMono(String.class)
                .map(body -> {
                    log.info("Sucesso na Validação");
                    return true;
                }) // Se chegou aqui, o token é válido
                .onErrorResume(e -> {
                    log.error("Erro Genérico no Token");
                    return Mono.just(false); // Tratamento de erro genérico
                });
    }
}
