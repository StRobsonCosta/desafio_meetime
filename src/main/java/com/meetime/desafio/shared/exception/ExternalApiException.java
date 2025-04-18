package com.meetime.desafio.shared.exception;

import org.springframework.web.reactive.function.client.WebClientResponseException;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String message) {
        super(message);
    }

  public ExternalApiException(String message, Exception e) {
  }
}
