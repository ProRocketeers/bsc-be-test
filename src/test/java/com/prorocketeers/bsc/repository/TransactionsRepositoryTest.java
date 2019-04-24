package com.prorocketeers.bsc.repository;

import com.prorocketeers.bsc.model.Transaction;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionsRepositoryTest {

  private TransactionsRepository transactionsRepository;

  @BeforeEach
  void beforeEach() {
    transactionsRepository = new InMemoryTransactionsRepository();
  }

  @Test
  void transactionsGetStored() {
    val a = new Transaction("USD", new BigDecimal("22"));
    val b = new Transaction("USD", new BigDecimal("20"));
    val expected = new Transaction("USD", new BigDecimal("42"));

    transactionsRepository.save(a);
    transactionsRepository.save(b);

    assertTrue(transactionsRepository.getStatus().contains(expected));
  }

  @Test
  void transactionsGetStoredOmittingZeroAmounts() {
    val a = new Transaction("CZK", new BigDecimal("0"));
    val b = new Transaction("ABC", new BigDecimal("00"));
    val notExpected = new Transaction("CZK", new BigDecimal("0"));
    val notExpectedToo = new Transaction("CZK", new BigDecimal("00"));

    transactionsRepository.save(a);
    transactionsRepository.save(b);

    assertFalse(transactionsRepository.getStatus().contains(notExpected));
    assertFalse(transactionsRepository.getStatus().contains(notExpectedToo));
  }

}