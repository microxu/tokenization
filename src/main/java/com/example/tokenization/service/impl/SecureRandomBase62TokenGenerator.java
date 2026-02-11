package com.example.tokenization.service.impl;

import com.example.tokenization.service.TokenGenerator;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The type Secure random base 62 token generator.
 */
@Service
public class SecureRandomBase62TokenGenerator implements TokenGenerator {

  private static final char[] ALPHABET =
      "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

  private final SecureRandom random = new SecureRandom();
  private final int length;

  /**
   * Instantiates a new Secure random base 62 token generator.
   *
   * @param length the length
   */
  public SecureRandomBase62TokenGenerator(@Value("${token.generator.length:32}") int length) {
    this.length = length;
  }

  @Override
  public String generate() {
    char[] out = new char[length];
    for (int i = 0; i < length; i++) {
      out[i] = ALPHABET[random.nextInt(ALPHABET.length)];
    }
    return new String(out);
  }
}
