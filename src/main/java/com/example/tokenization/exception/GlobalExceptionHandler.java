package com.example.tokenization.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.tokenization.models.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * The type Global exception handler.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle store unavailable response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(TokenizationException.class)
  public ResponseEntity<ErrorResponse> handleStoreUnavailable(
      TokenizationException ex) {

    log.error("Token store unavailable", ex);

    return ResponseEntity.status(ex.getStatusCode())
        .body(ErrorResponse.of(
            "TOKEN_STORE_UNAVAILABLE",
            ex.getMessage()
        ));
  }

  /**
   * Handle store unavailable response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(DeTokenException.class)
  public ResponseEntity<ErrorResponse> handleStoreUnavailable(
      DeTokenException ex) {

    log.error("DeToken store unavailable", ex);

    return ResponseEntity.status(ex.getStatusCode())
        .body(ErrorResponse.of(
            "DeTOKEN_STORE_UNAVAILABLE",
            ex.getMessage()
        ));
  }

  /**
   * Handle data access response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException ex) {

    log.error("Database error", ex);

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(ErrorResponse.of(
            "DATABASE_ERROR",
            "Temporary database error"
        ));
  }

  /**
   * Handle generic response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

    log.error("Unexpected error", ex);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of(
            "INTERNAL_ERROR",
            "Unexpected server error"
        ));
  }
}
