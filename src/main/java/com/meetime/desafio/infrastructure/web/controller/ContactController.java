package com.meetime.desafio.infrastructure.web.controller;

import com.meetime.desafio.application.dto.ContactDto;
import com.meetime.desafio.application.usecase.ContactUseCase;
import com.meetime.desafio.domain.model.Contact;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactUseCase contactUseCase;

    @PostMapping
    public ResponseEntity<?> createContact(@Valid @RequestBody ContactDto dto) {
        Contact contact = Contact.builder()
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .email(dto.email())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(contactUseCase.createContact(contact));
    }

    @GetMapping
    public ResponseEntity<List<Contact>> listContact() {
        return ResponseEntity.ok(contactUseCase.listContact());
    }
}
