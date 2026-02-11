package com.example.tokenization.controller;

import com.example.tokenization.service.TokenizationService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * The type Tokenization controller.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenizationController {

  private final TokenizationService service;

  /**
   * Instantiates a new Tokenization controller.
   *
   * @param service the service
   */
  public TokenizationController(TokenizationService service) {
    this.service = service;
  }

  /**
   * Tokenize list.
   *
   * @param accountNumbers the account numbers
   * @return the list
   */
  @PostMapping(path = "/tokenize", consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<String> tokenize(@RequestBody List<String> accountNumbers) {
    if (CollectionUtils.isEmpty(accountNumbers)){
      return List.of();
    }

    return service.tokenize(accountNumbers);
  }

  /**
   * Detokenize list.
   *
   * @param tokens the tokens
   * @return the list
   */
  @PostMapping(path = "/detokenize", consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<String> detokenize(@RequestBody List<String> tokens) {
    if(CollectionUtils.isEmpty(tokens)){
      return  List.of();
    }
    return service.detokenize(tokens);
  }

}

