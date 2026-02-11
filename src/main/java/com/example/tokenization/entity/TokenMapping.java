package com.example.tokenization.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import java.time.Instant;

/**
 * The type Token mapping.
 */
@Entity
@Table(
    name = "token_mapping",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_token", columnNames = "token"),
        @UniqueConstraint(name = "uk_account", columnNames = "accountNumber")
    }
)
public class TokenMapping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 64)
  private String token;

  @Column(nullable = false, length = 64)
  private String accountNumber;

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  /**
   * Instantiates a new Token mapping.
   */
  protected TokenMapping() {}

  /**
   * Instantiates a new Token mapping.
   *
   * @param token         the token
   * @param accountNumber the account number
   */
  public TokenMapping(String token, String accountNumber) {
    this.token = token;
    this.accountNumber = accountNumber;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() { return id; }

  /**
   * Gets token.
   *
   * @return the token
   */
  public String getToken() { return token; }

  /**
   * Gets account number.
   *
   * @return the account number
   */
  public String getAccountNumber() { return accountNumber; }

  /**
   * Gets created at.
   *
   * @return the created at
   */
  public Instant getCreatedAt() { return createdAt; }
}

