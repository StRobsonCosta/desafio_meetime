package com.meetime.desafio.domain.port.in;

import com.meetime.desafio.application.dto.ContactDto;
import com.meetime.desafio.domain.model.Contact;

import java.util.List;

public interface ContactUseCase {

    ContactDto createContact(Contact contact);
    List<Contact> listContact();
}
