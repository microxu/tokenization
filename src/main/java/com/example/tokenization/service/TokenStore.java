package com.example.tokenization.service;


import java.util.Optional;

/**
 * The interface Token store.
 */
public interface TokenStore {
  /**
   * Tokenize string.
   *
   * @param accountNumber the account number
   * @return the string
   */
  String tokenize(String accountNumber);

  /**
   * Detokenize optional.
   *
   * @param token the token
   * @return the optional
   */
  Optional<String> detokenize(String token);
}

