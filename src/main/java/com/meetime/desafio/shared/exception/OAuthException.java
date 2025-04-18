package com.meetime.desafio.shared.exception;

public class OAuthException extends RuntimeException {
    public OAuthException(String message) {
        super(message);
    }

  public OAuthException(String message, Throwable cause) {
    super(message, cause);
  }
}
