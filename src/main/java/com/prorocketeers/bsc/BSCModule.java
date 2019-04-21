package com.prorocketeers.bsc;

import com.google.inject.AbstractModule;
import com.prorocketeers.bsc.repositories.ExchangeRateRepository;
import com.prorocketeers.bsc.repositories.InMemoryExchangeRateRepository;
import com.prorocketeers.bsc.repositories.InMemoryTransactionsRepository;
import com.prorocketeers.bsc.repositories.TransactionsRepository;

public class BSCModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TransactionsRepository.class).toInstance(new InMemoryTransactionsRepository());
        bind(ExchangeRateRepository.class).toInstance(new InMemoryExchangeRateRepository());
    }
}
