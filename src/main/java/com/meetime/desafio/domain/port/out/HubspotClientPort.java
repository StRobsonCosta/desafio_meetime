package com.meetime.desafio.domain.port.out;

import com.meetime.desafio.application.dto.ContactDto;
import com.meetime.desafio.domain.model.Contact;

import java.util.List;

public interface HubspotClientPort {
    ContactDto createContact(Contact contact);
    List<Contact> listContacts();
}
