package com.example.tokenization.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.tokenization.service.impl.SecureRandomBase62TokenGenerator;

class SecureRandomBase62TokenGeneratorTest {

  @Test
  void generate_returnsFixedLength_andOnlyBase62Chars() {
    //GIVEN
    int length = 32;
    var gen = new SecureRandomBase62TokenGenerator(length);

    //WHEN
    String token = gen.generate();

    //THEN
    assertNotNull(token);
    assertEquals(length, token.length());
  }

  @Test
  void generate_notAlwaysSame() {
    //GIVEN
    var gen = new SecureRandomBase62TokenGenerator(32);

    //WHEN
    String t1 = gen.generate();
    String t2 = gen.generate();

    //THEN
    assertNotEquals(t1, t2);
  }

}
