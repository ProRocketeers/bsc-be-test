package com.prorocketeers.bsc.repository;

import com.prorocketeers.bsc.model.Transaction;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Interface defining ExchangeRateRepository
 */
public interface ExchangeRateRepository {

  /**
   * Gets exchange rate based of input params.
   *
   * @param from {@link Transaction#currencyCode}
   * @param to {@link Transaction#currencyCode}
   * @return Optional exchange rate.
   */
  Optional<BigDecimal> get(String from, String to);

  /**
   * Updates (if exists) or inserts (if it doesnt) one exchange rate.
   *
   * @param from {@link Transaction#currencyCode}
   * @param to {@link Transaction#currencyCode}
   * @param rate exchange rate for specified currency pair
   */
  void upsert(String from, String to, BigDecimal rate);
}