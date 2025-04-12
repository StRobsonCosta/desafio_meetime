package com.meetime.desafio.infrastructure.web.controller;

import com.meetime.desafio.application.dto.ContatoRequestDto;
import com.meetime.desafio.application.usecase.ContatoUseCase;
import com.meetime.desafio.domain.model.Contato;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContatoController {

    private final ContatoUseCase contactUseCase;

    @PostMapping
    public void createContact(@Valid @RequestBody ContatoRequestDto dto) {
        Contato contato = Contato.builder()
                .nome(dto.nome())
                .sobreNome(dto.sobreNome())
                .email(dto.email())
                .build();

        contactUseCase.criarContato(contato);
    }
}
