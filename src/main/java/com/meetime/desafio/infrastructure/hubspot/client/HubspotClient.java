package com.meetime.desafio.infrastructure.hubspot.client;

import com.meetime.desafio.domain.model.Contato;
import com.meetime.desafio.domain.port.out.HubspotClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HubspotClient implements HubspotClientPort {

    private final WebClient hubspotClient;

    @Override
    public void createContact(Contato contact) {
        Map<String, Object> payload = Map.of(
                "properties", Map.of(
                        "firstname", contact.getNome(),
                        "lastname", contact.getSobreNome(),
                        "email", contact.getEmail()
                )
        );

        hubspotClient.post()
                .uri("/crm/v3/objects/contacts")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
