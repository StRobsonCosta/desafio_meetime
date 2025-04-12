package com.meetime.desafio.application.service;

import com.meetime.desafio.application.usecase.ContatoUseCase;
import com.meetime.desafio.domain.model.Contato;
import com.meetime.desafio.domain.port.out.HubspotClientPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ContatoService implements ContatoUseCase {

    private final HubspotClientPort hubspotClientPort;

    @Override
    public void criarContato(Contato contato) {
        // Aqui ter lógica de validação, rate limit e talz.
        hubspotClientPort.createContact(contato);
    }
}
