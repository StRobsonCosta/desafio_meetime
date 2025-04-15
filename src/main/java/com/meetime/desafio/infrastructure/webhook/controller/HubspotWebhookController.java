package com.meetime.desafio.infrastructure.webhook.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks/hubspot")
public class HubspotWebhookController {

    @PostMapping
    public void handleWebhook(@RequestBody String payload) {
        //  criar DTO para deserializar e validar.
        System.out.println("Received webhook payload: " + payload);
    }
}
