package com.example.tokenization.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.tokenization.entity.TokenMapping;
import com.example.tokenization.exception.TokenizationException;
import com.example.tokenization.repository.TokenMappingRepository;
import com.example.tokenization.service.impl.JpaTokenStore;

@ExtendWith(MockitoExtension.class)
class JpaTokenStoreTest {

  @Mock
  TokenMappingRepository repo;
  @Mock
  TokenGenerator generator;

  @InjectMocks
  JpaTokenStore store;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(store, "maxRetries", 3);
  }

  @Test
  void tokenize_blank_returnsNull() {
    assertNull(store.tokenize(null));
    assertNull(store.tokenize(""));
    assertNull(store.tokenize("   "));
    verifyNoInteractions(repo, generator);
  }

  @Test
  void tokenize_existingMapping_returnsExistingToken_withoutInsert() {
    //GIVEN
    when(repo.findByAccountNumber("acc")).thenReturn(Optional.of(new TokenMapping("tok", "acc")));

    //WHEN & THEN
    assertEquals("tok", store.tokenize("acc"));

    verify(repo).findByAccountNumber("acc");
    verifyNoMoreInteractions(repo);
    verifyNoInteractions(generator);
  }

  @Test
  void tokenize_newMapping_savesAndReturnsGeneratedToken() {
    //GIVEN
    when(repo.findByAccountNumber("acc")).thenReturn(Optional.empty());
    when(generator.generate()).thenReturn("T".repeat(32));

    //WHEN
    String token = store.tokenize("acc");

    //THEN
    assertEquals("T".repeat(32), token);
    verify(repo).saveAndFlush(any(TokenMapping.class));
  }

  @Test
  void tokenize_onIntegrityViolation_thenRereadAccountNumberAndReturnWinnerToken() {
    //GIVEN
    when(repo.findByAccountNumber("acc"))
        .thenReturn(Optional.empty()) // first read
        .thenReturn(Optional.of(new TokenMapping("WINNER", "acc"))); // reread after exception

    //WHEN & THEN
    when(generator.generate()).thenReturn("A".repeat(32));
    doThrow(new DataIntegrityViolationException("dup"))
        .when(repo).saveAndFlush(any(TokenMapping.class));

    String token = store.tokenize("acc");

    assertEquals("WINNER", token);
    verify(repo, times(2)).findByAccountNumber("acc");
  }

  @Test
  void tokenize_retriesUntilExhausted_thenThrowsTokenizationException() {
    //GIVEN
    when(repo.findByAccountNumber("acc")).thenReturn(Optional.empty());
    when(generator.generate()).thenReturn("A".repeat(32), "B".repeat(32), "C".repeat(32));

    doThrow(new DataIntegrityViolationException("dup"))
        .when(repo).saveAndFlush(any(TokenMapping.class));

    //WHEN &THEN
    TokenizationException ex =
        assertThrows(TokenizationException.class, () -> store.tokenize("acc"));

    assertEquals("Unable to create unique token for account acc after retries", ex.getMessage());
    assertEquals(500, ex.getStatusCode());
    verify(generator, times(3)).generate();
  }
}
