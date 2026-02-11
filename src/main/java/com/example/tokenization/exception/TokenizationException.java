package com.example.tokenization.exception;

import lombok.Getter;

/**
 * The type Tokenization exception.
 */
@Getter
public class TokenizationException extends RuntimeException {
  private final int statusCode;

  /**
   * Instantiates a new Tokenization exception.
   *
   * @param message    the message
   * @param statusCode the status code
   */
  public TokenizationException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }
}
