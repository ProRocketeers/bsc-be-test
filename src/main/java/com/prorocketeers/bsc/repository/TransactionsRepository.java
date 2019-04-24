package com.prorocketeers.bsc.repository;

import com.prorocketeers.bsc.model.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface TransactionsRepository {

  /**
   * @return Collection of transactions with unique {@link Transaction#currencyCode} where amount is a sum of all transactions with that code.
   */
  Collection<Transaction> getStatus();

  /**
   * Store the transaction.
   *
   * @param transaction to be stored
   */
  void save(@NotNull Transaction transaction);
}