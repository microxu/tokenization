package com.example.tokenization.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.example.tokenization.exception.GlobalExceptionHandler;
import com.example.tokenization.service.TokenizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TokenizationController.class)
@Import({GlobalExceptionHandler.class, TokenizationControllerTest.TestConfig.class})
class TokenizationControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  TokenizationService service;

  @Autowired
  ObjectMapper objectMapper;

  @TestConfiguration
  static class TestConfig {

    @Bean
    TokenizationService tokenizationService() {
      return Mockito.mock(TokenizationService.class);
    }
  }

  @Test
  void tokenize_success() throws Exception {
    //GIVEN
    when(service.tokenize(List.of("a1", "a2")))
        .thenReturn(List.of("t1", "t2"));

    //WHEN & THEN
    mockMvc.perform(
            post("/tokenize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of("a1", "a2")))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]").value("t1"));
  }
}


