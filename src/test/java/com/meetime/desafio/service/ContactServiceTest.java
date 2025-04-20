package com.meetime.desafio.service;

import com.meetime.desafio.application.dto.ContactDto;
import com.meetime.desafio.application.service.ContactService;
import com.meetime.desafio.domain.model.Contact;
import com.meetime.desafio.domain.port.out.HubspotClientPort;
import com.meetime.desafio.domain.port.out.TokenRepository;
import com.meetime.desafio.infrastructure.config.RateLimiter;
import com.meetime.desafio.shared.exception.ContactException;
import com.meetime.desafio.shared.exception.ExternalApiException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    private HubspotClientPort hubspotClientPort;

    @Mock
    private TokenRepository tokenStore;

    @Mock
    private RateLimiter rateLimiter;

    @InjectMocks
    private ContactService contactService;

    @NotNull
    private static Contact getContact() {
        return new Contact("john","Doe","john@example.com");
    }

    @NotNull
    private static ContactDto getContactDto() {
        return new ContactDto("john","Doe", "john@example.com");
    }

    @Test
    void shouldCreateContactSuccessfully() {
        final Contact contact = getContact();
        final ContactDto expectedResponse = getContactDto();

        Mockito.when(tokenStore.hasToken()).thenReturn(true);
        Mockito.when(rateLimiter.tryAcquire()).thenReturn(true);
        Mockito.when(hubspotClientPort.createContact(contact)).thenReturn(expectedResponse);

        ContactDto result = contactService.createContact(contact);

        assertNotNull(result);
        assertEquals("Doe", result.lastname());
        assertEquals("john@example.com", result.email());

        Mockito.verify(hubspotClientPort).createContact(contact);
    }

    @Test
    void shouldThrowExceptionWhenTokenMissing() {
        final Contact contact = getContact();

        Mockito.when(tokenStore.hasToken()).thenReturn(false);

        assertThrows(ContactException.class, () -> contactService.createContact(contact));

        Mockito.verify(hubspotClientPort, never()).createContact(any());
    }

    @Test
    void shouldListContactsSuccessfully() {
        List<Contact> contacts = List.of(getContact(), new Contact("Mary","Anne","ma@example.com"));
        Mockito.when(hubspotClientPort.listContacts()).thenReturn(contacts);

        List<Contact> result = contactService.listContact();

        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExternalApiExceptionWhenListFails() {
        Mockito.when(hubspotClientPort.listContacts())
                .thenThrow(WebClientResponseException.create(503, "Service Unavailable", null, null, null));

        assertThrows(ExternalApiException.class, () -> contactService.listContact());
    }

    @Test
    void shouldThrowContactExceptionWhenListFailsGeneric() {
        Mockito.when(hubspotClientPort.listContacts()).thenThrow(new RuntimeException("fail"));

        assertThrows(ContactException.class, () -> contactService.listContact());
    }

    // Testes de validação
    @Test
    void shouldThrowExceptionWhenContactIsNull() {
        assertThrows(ContactException.class, () -> contactService.createContact(null));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        final Contact contact = getContact();
        assertThrows(ContactException.class, () -> contactService.createContact(contact));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        final Contact contact = getContact();
        assertThrows(ContactException.class, () -> contactService.createContact(contact));
    }
}
