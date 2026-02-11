package com.example.tokenization.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.tokenization.exception.DeTokenException;

import lombok.extern.slf4j.Slf4j;

/**
 * The type Tokenization service.
 */
@Slf4j
@Service
public class TokenizationService {

  private final TokenStore tokenStore;

  /**
   * Instantiates a new Tokenization service.
   *
   * @param tokenStore the token store
   */
  public TokenizationService(TokenStore tokenStore) {
    this.tokenStore = tokenStore;
  }

  /**
   * Tokenize list.
   *
   * @param accounts the accounts
   * @return the list
   */
  public List<String> tokenize(List<String> accounts) {

    log.info("Tokenize request received: count={}", accounts.size());
    return accounts.stream().map(tokenStore::tokenize).toList();
  }

  /**
   * Detokenize list.
   *
   * @param tokens the tokens
   * @return the list
   */
  public List<String> detokenize(List<String> tokens) {

    log.info("Detokenize request received: {}", tokens.size());

    List<String> result =
        tokens.stream()
            .map(t -> tokenStore.detokenize(t).orElse(null))
            .toList();

    boolean anyFound = result.stream().anyMatch(v -> v != null);

    if (!anyFound) {
      throw new DeTokenException("No tokens found", HttpStatus.NOT_FOUND.value());
    }
    return result;
  }
}

