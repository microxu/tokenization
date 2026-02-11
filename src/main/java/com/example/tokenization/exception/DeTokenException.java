package com.example.tokenization.exception;

import lombok.Getter;

/**
 * The type De token exception.
 */
@Getter
public class DeTokenException extends RuntimeException {
  private final int statusCode;

  /**
   * Instantiates a new De token exception.
   *
   * @param message    the message
   * @param statusCode the status code
   */
  public DeTokenException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }
}
