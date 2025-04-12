package com.meetime.desafio.domain.port.out;

import com.meetime.desafio.domain.model.Contato;

public interface HubspotClientPort {
    void createContact(Contato contato);
}
