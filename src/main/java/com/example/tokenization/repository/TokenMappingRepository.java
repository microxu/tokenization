package com.example.tokenization.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tokenization.entity.TokenMapping;

/**
 * The interface Token mapping repository.
 */
public interface TokenMappingRepository extends JpaRepository<TokenMapping, Long> {
  /**
   * Find by account number optional.
   *
   * @param accountNumber the account number
   * @return the optional
   */
  Optional<TokenMapping> findByAccountNumber(String accountNumber);

  /**
   * Find by token optional.
   *
   * @param token the token
   * @return the optional
   */
  Optional<TokenMapping> findByToken(String token);
}

