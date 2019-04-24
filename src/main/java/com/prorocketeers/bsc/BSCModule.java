package com.prorocketeers.bsc;

import com.google.inject.AbstractModule;
import com.prorocketeers.bsc.repository.ExchangeRateRepository;
import com.prorocketeers.bsc.repository.InMemoryExchangeRateRepository;
import com.prorocketeers.bsc.repository.InMemoryTransactionsRepository;
import com.prorocketeers.bsc.repository.TransactionsRepository;

public class BSCModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TransactionsRepository.class).toInstance(new InMemoryTransactionsRepository());
    bind(ExchangeRateRepository.class).toInstance(new InMemoryExchangeRateRepository());
  }

}