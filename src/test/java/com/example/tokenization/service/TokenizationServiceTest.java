package com.example.tokenization.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tokenization.exception.DeTokenException;

@ExtendWith(MockitoExtension.class)
class TokenizationServiceTest {

  @Mock TokenStore tokenStore;

  @InjectMocks TokenizationService service;

  @Test
  void tokenize_mapsEachAccount_usingStore() {
    //GIVEN
    when(tokenStore.tokenize("a1")).thenReturn("t1");
    when(tokenStore.tokenize("a2")).thenReturn("t2");

    //WHEN
    var result = service.tokenize(List.of("a1", "a2"));

    //THEN
    assertEquals(List.of("t1", "t2"), result);
    verify(tokenStore).tokenize("a1");
    verify(tokenStore).tokenize("a2");
  }

  @Test
  void detokenize_whenSomeFound_returnsListIncludingNulls() {
    //GIVEN
    when(tokenStore.detokenize("t1")).thenReturn(Optional.of("a1"));
    when(tokenStore.detokenize("t2")).thenReturn(Optional.empty());

    //WHEN
    var result = service.detokenize(List.of("t1", "t2"));

    //THEN
    assertEquals(2, result.size());
    assertEquals("a1", result.get(0));
    assertNull(result.get(1));

  }

  @Test
  void detokenize_whenNoneFound_throwsDeTokenException() {
    //GIVEN
    when(tokenStore.detokenize(anyString())).thenReturn(Optional.empty());

    //WHEN & THEN
    DeTokenException ex =
        assertThrows(DeTokenException.class, () -> service.detokenize(List.of("t1", "t2")));

    assertEquals("No tokens found", ex.getMessage());
    assertEquals(404, ex.getStatusCode());
  }
}

