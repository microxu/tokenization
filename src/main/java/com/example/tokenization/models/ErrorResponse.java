package com.example.tokenization.models;

/**
 * The type Error response.
 */
public record ErrorResponse(
    String error,
    String message
) {
  /**
   * Of error response.
   *
   * @param error   the error
   * @param message the message
   * @return the error response
   */
  public static ErrorResponse of(String error, String message) {
    return new ErrorResponse(error, message);
  }
}

