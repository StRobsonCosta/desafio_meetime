package com.meetime.desafio.application.service;

import com.meetime.desafio.application.dto.ContactDto;
import com.meetime.desafio.domain.port.in.ContactUseCase;
import com.meetime.desafio.domain.model.Contact;
import com.meetime.desafio.domain.port.out.HubspotClientPort;
import com.meetime.desafio.domain.port.out.TokenRepository;
import com.meetime.desafio.infrastructure.config.RateLimiter;
import com.meetime.desafio.shared.exception.ContactException;
import com.meetime.desafio.shared.exception.ExternalApiException;
import com.meetime.desafio.shared.exception.RateLimitExceededException;
import com.meetime.desafio.shared.exception.TokenNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ContactService implements ContactUseCase {

    private final HubspotClientPort hubspotClientPort;
    private final TokenRepository tokenStore;
    private final RateLimiter rateLimiter;

    @Override
    public ContactDto createContact(Contact contact) {
        try {

        this.validate(contact);

        if (!tokenStore.hasToken())
            throw new TokenNotFoundException("Token OAuth ausente. Faça login para obter um novo token.");

        if (!rateLimiter.tryAcquire())
            throw new RateLimitExceededException("Limite de requisições excedido. Tente novamente mais tarde.");

        return hubspotClientPort.createContact(contact);

        } catch (ExternalApiException e) {
            throw e;
        } catch (WebClientResponseException e) {
            throw new ExternalApiException("Erro ao criar contato no Hubspot: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ContactException("Erro inesperado ao criar contato", e);
        }
    }

    @Override
    public List<Contact> listContact() {
        try {
            return hubspotClientPort.listContacts();

        } catch (WebClientResponseException e) {
            throw new ExternalApiException("Erro ao listar contatos do Hubspot: " + e.getMessage(), e);

        } catch (Exception e) {
            throw new ContactException("Erro inesperado ao listar contatos", e);
        }
    }

    public void validate(Contact contact) {
        if(Objects.isNull(contact))
            throw new ContactException("Contato inválido ou nulo");

        if (Objects.isNull(contact.getEmail()) || contact.getEmail().isBlank())
            throw new ContactException("E-mail é obrigatório.");

        if (!contact.getEmail().contains("@"))
            throw new ContactException("E-mail inválido.");

    }
}
