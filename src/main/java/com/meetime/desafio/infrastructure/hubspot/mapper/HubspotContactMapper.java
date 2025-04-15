package com.meetime.desafio.infrastructure.hubspot.mapper;

import com.meetime.desafio.application.dto.ContactDto;
import com.meetime.desafio.application.dto.HubspotContact;
import com.meetime.desafio.domain.model.Contact;

public class HubspotContactMapper {

    public static Contact toDomain(HubspotContact hubspotContact) {
        return Contact.builder()
                .firstname(hubspotContact.getProperties().getFirstname())
                .lastname(hubspotContact.getProperties().getLastname())
                .email(hubspotContact.getProperties().getEmail())
                .build();
    }

    public static ContactDto toDto(HubspotContact hubspotContact) {
        return new ContactDto(hubspotContact.getProperties().getFirstname(),
                hubspotContact.getProperties().getLastname(),
                hubspotContact.getProperties().getEmail());

    }
}
