package com.example.tokenization.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void handleTokenizationException() {
    var resp = handler.handleStoreUnavailable(new TokenizationException("token generating error", 500));

    assertEquals(500, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
    assertEquals("TOKEN_STORE_UNAVAILABLE", resp.getBody().error());
  }

  @Test
  void handleDeTokenizationException() {
    var resp = handler.handleStoreUnavailable(new DeTokenException("detoken generating error", 500));

    assertEquals(500, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
    assertEquals("DeTOKEN_STORE_UNAVAILABLE", resp.getBody().error());
  }

  @Test
  void handleDataAccessError() {
    var resp = handler.handleDataAccess(new DataAccessResourceFailureException("db down"));

    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, resp.getStatusCode());
    assertNotNull(resp.getBody());
    assertEquals("DATABASE_ERROR", resp.getBody().error());
  }
}

