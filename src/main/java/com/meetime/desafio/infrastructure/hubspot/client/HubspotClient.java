package com.meetime.desafio.infrastructure.hubspot.client;

import com.meetime.desafio.application.dto.ContactDto;
import com.meetime.desafio.application.dto.HubspotContact;
import com.meetime.desafio.application.dto.HubspotResponse;
import com.meetime.desafio.domain.model.Contact;
import com.meetime.desafio.domain.port.out.HubspotClientPort;
import com.meetime.desafio.infrastructure.hubspot.mapper.HubspotContactMapper;
import com.meetime.desafio.infrastructure.persistence.TokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class HubspotClient implements HubspotClientPort {

    @Autowired
    private final WebClient hubspotClient;
    private final TokenStore tokenStore;

    @Override
    public ContactDto createContact(Contact contact) {
        if (!tokenStore.hasToken()) {
            throw new IllegalStateException("Token de acesso não encontrado. Faça a autenticação OAuth.");
        }

        Map<String, Object> payload = Map.of(
                "properties", Map.of(
                        "firstname", contact.getFirstname(),
                        "lastname", contact.getLastname(),
                        "email", contact.getEmail()
                )
        );

        HubspotContact response = hubspotClient.post()
                .uri("/crm/v3/objects/contacts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenStore.getAccessToken())
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(HubspotContact.class)
                .block();

        if (Objects.isNull(response) || Objects.isNull(response.getProperties()))
            throw new IllegalStateException("Erro ao criar contato no HubSpot");

        return HubspotContactMapper.toDto(response);
    }

    @Override
    public List<Contact> listContacts() {
        String accessToken = tokenStore.getAccessToken();

        HubspotResponse response = hubspotClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/crm/v3/objects/contacts")
                        .queryParam("limit", 10)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(HubspotResponse.class)
                .block();

        if (Objects.isNull(response) || Objects.isNull(response.getResults()))
            return List.of();

        return response.getResults().stream()
                .map(HubspotContactMapper::toDomain)
                .toList();
    }
}
