package com.prorocketeers.bsc.models;

import com.prorocketeers.bsc.exception.TransactionParseException;
import com.prorocketeers.bsc.model.Transaction;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

  @Test
  void validateCorrectInput() throws TransactionParseException {
    Transaction.validateInput("USD 42");
  }

  @Test
  void validateCorrectNegativeInput() throws TransactionParseException {
    Transaction.validateInput("USD -42");
  }

  @Test
  void validateCorrectPositiveInput() throws TransactionParseException {
    Transaction.validateInput("USD +42");
  }

  @Test
  void validateCorrectDecimalInput() throws TransactionParseException {
    Transaction.validateInput("USD 42.42");
  }

  @Test
  void validateIncorrectInputWrongDecimal() {
    assertThrows(TransactionParseException.class, () -> Transaction.validateInput("USD 123.3.4"));
  }

  @Test
  void validateIncorrectInputLowerCase() {
    assertThrows(TransactionParseException.class, () -> Transaction.validateInput("usd 123"));
  }

  @Test
  void validateIncorrectInputOnly2Letters() {
    assertThrows(TransactionParseException.class, () -> Transaction.validateInput("US 123"));
  }

  @Test
  void validateIncorrectInput() {
    assertThrows(TransactionParseException.class, () -> Transaction.validateInput("USD"));
  }

  @Test
  void fromInputValid() {
    val expected = new Transaction("USD", new BigDecimal("42"));
    assertEquals(expected, Transaction.fromInput("USD 42"));
  }

  @Test
  void fromInputNegative() {
    val expected = new Transaction("USD", new BigDecimal("-42"));
    assertEquals(expected, Transaction.fromInput("USD -42"));
  }

  @Test
  void fromInputPositive() {
    val expected = new Transaction("USD", new BigDecimal("42"));
    assertEquals(expected, Transaction.fromInput("USD +42"));
  }

  @Test
  void add() {
    val a = new Transaction("USD", new BigDecimal("20"));
    val b = new Transaction("USD", new BigDecimal("22"));
    val expected = new Transaction("USD", new BigDecimal("42"));
    assertEquals(expected, a.add(b));
  }

}