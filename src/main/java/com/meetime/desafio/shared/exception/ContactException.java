package com.meetime.desafio.shared.exception;

public class ContactException extends RuntimeException {
    public ContactException(String message) {
        super(message);
    }

    public ContactException(String message, Exception e) {
    }
}
