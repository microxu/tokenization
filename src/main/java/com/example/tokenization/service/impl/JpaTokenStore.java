package com.example.tokenization.service.impl;

import com.example.tokenization.entity.TokenMapping;
import com.example.tokenization.exception.TokenizationException;
import com.example.tokenization.service.TokenGenerator;
import com.example.tokenization.service.TokenStore;
import com.example.tokenization.repository.TokenMappingRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * The type Jpa token store.
 */
@Service
@Slf4j
public class JpaTokenStore implements TokenStore {

  private final TokenMappingRepository repo;
  private final TokenGenerator generator;

  @Value("${token.store.retry.max-attempts:3}")
  private int maxRetries;

  /**
   * Instantiates a new Jpa token store.
   *
   * @param repo      the repo
   * @param generator the generator
   */
  public JpaTokenStore(
      TokenMappingRepository repo,
      TokenGenerator generator) {
    this.repo = repo;
    this.generator = generator;
  }

  @Override
  @Transactional
  public String tokenize(String accountNumber) {
    if (accountNumber == null || accountNumber.isBlank()) return null;

    // Read first
    var existing = repo.findByAccountNumber(accountNumber);
    if (existing.isPresent()) return existing.get().getToken();

    return insertWithRetry(accountNumber);
  }

  private String insertWithRetry(String accountNumber) {
    for (int i = 0; i < maxRetries; i++) {
      String token = generator.generate(); // fixed-length base62

      try {
        repo.saveAndFlush(new TokenMapping(token, accountNumber));
        return token;
      } catch (DataIntegrityViolationException e) {
        // re-read and return winner token
        var again = repo.findByAccountNumber(accountNumber);
        if (again.isPresent()) {
          log.debug("Concurrent insert detected for accountNumber; returning existing token");
          return again.get().getToken();
        }
        // token collision, retry generate token
        log.debug("Token collision or integrity violation, retrying (attempt {})", i + 1);
      }
    }

    log.warn(
        "Failed to generate unique token for account {} after {} retries",
        String.format("%4s",
            accountNumber.length() <= 4
                ? accountNumber
                : accountNumber.substring(accountNumber.length() - 4)
        ).replace(' ', '*'),
        maxRetries
    );

    throw new TokenizationException(String.format("Unable to create unique token for account %s after retries", accountNumber)
        ,HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @Override
  @Transactional
  public Optional<String> detokenize(String token) {
    if (token == null || token.isBlank()) return Optional.empty();
    return repo.findByToken(token).map(TokenMapping::getAccountNumber);
  }
}

