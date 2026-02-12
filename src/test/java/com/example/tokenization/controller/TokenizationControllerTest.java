package com.example.tokenization.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.example.tokenization.exception.DeTokenException;
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
        .andExpect(status().isOk());
  }

  @Test
  void tokenize_emptyList_returnsEmpty() throws Exception {
    mockMvc
        .perform(
            post("/tokenize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of())))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
  }

  @Test
  void detokenize_success() throws Exception {
    // GIVEN
    var resp = new java.util.ArrayList<String>();
    resp.add("a1");
    resp.add(null);
    when(service.detokenize(List.of("t1", "t2"))).thenReturn(resp);

    // WHEN & THEN
    mockMvc
        .perform(
            post("/detokenize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of("t1", "t2"))))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0]").value("a1"))
        .andExpect(jsonPath("$[1]").value(nullValue()));

    verify(service).detokenize(List.of("t1", "t2"));
  }

  @Test
  void detokenize_noneFound_returns404() throws Exception {
    // GIVEN
    when(service.detokenize(List.of("t1", "t2")))
        .thenThrow(new DeTokenException("No tokens found", 404));

    // WHEN & THEN
    mockMvc
        .perform(
            post("/detokenize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of("t1", "t2"))))
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("DeTOKEN_STORE_UNAVAILABLE"))
        .andExpect(jsonPath("$.message").value("No tokens found"));
  }

  @Test
  void detokenize_emptyList_returnsEmpty() throws Exception {
    mockMvc.perform(
            post("/detokenize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of())))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }


}


