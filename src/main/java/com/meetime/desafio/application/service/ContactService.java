package com.meetime.desafio.application.service;

import com.meetime.desafio.application.dto.ContactDto;
import com.meetime.desafio.application.usecase.ContactUseCase;
import com.meetime.desafio.domain.model.Contact;
import com.meetime.desafio.domain.port.out.HubspotClientPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ContactService implements ContactUseCase {

    private final HubspotClientPort hubspotClientPort;

    @Override
    public ContactDto createContact(Contact contact) {
        // Aqui ter lógica de validação, rate limit e talz.
        return hubspotClientPort.createContact(contact);
    }

    @Override
    public List<Contact> listContact() {
        return hubspotClientPort.listContacts();
    }
}
