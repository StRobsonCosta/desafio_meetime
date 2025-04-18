package com.meetime.desafio.shared.exception;

public class TokenExchangeException extends RuntimeException {
    public TokenExchangeException(String message) {
        super(message);
    }

    public TokenExchangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
