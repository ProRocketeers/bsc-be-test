package com.prorocketeers.bsc;

import static com.prorocketeers.bsc.model.CurrencyCode.USD;

import com.google.inject.Guice;
import com.prorocketeers.bsc.model.CurrencyCode;
import com.prorocketeers.bsc.repository.ExchangeRateRepository;
import com.prorocketeers.bsc.repository.TransactionsRepository;
import com.prorocketeers.bsc.task.InputHandlerTimerTask;
import com.prorocketeers.bsc.task.OutputHandlerTimerTask;
import com.prorocketeers.bsc.util.InputFileUtility;
import lombok.extern.java.Log;
import lombok.val;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

@Log
public class Main {

  public static void main(String[] args) throws Exception {
    // Dependency injections
    val injector = Guice.createInjector(new BSCModule());
    val transactionsRepository = injector.getInstance(TransactionsRepository.class);
    val exchangeRateRepository = injector.getInstance(ExchangeRateRepository.class);

    // Arguments handling
    loadDataFromFile(args, transactionsRepository);

    // Hardcoded exchange rates
    initExchangeRates(exchangeRateRepository);

    // Input handling
    val scannerTimer = new Timer();
    val scannerPeriodInMs = 1000;
    System.out.println("Input your transaction:");
    TimerTask inputHandler = new InputHandlerTimerTask(transactionsRepository);
    scannerTimer.schedule(inputHandler, 0, scannerPeriodInMs);

    // Output handling regular transaction report
    val outputTimer = new Timer();
    val outputPeriodInMs = 10000;
    TimerTask outputHandler = new OutputHandlerTimerTask(transactionsRepository,
        exchangeRateRepository);
    outputTimer.schedule(outputHandler, 0, outputPeriodInMs);
  }

  /**
   * Loading data from file if it is specified.
   *
   * @param args - args[0] can be file path for loading init state
   */
  private static void loadDataFromFile(String[] args, TransactionsRepository transactionsRepository)
      throws Exception {
    if (args.length > 0) {
      val filePath = args[0];
      val loadedCurrencies = InputFileUtility.loadFileContentToTransactions(filePath);
      loadedCurrencies.forEach(transactionsRepository::save);
    }
  }

  /**
   * Saving of basic exchange rates specified in enum {@link CurrencyCode} into
   * {@param exchangeRateRepository}.
   */
  private static void initExchangeRates(ExchangeRateRepository exchangeRateRepository) {
    for (CurrencyCode currencyCode : CurrencyCode.values()) {
      // Condition because we converting all exchange rate instead of USD and in output report
      // we don't want e.g. "USD 500 (500 USD)" but "USD 500"
      if (currencyCode != USD) {
        exchangeRateRepository.upsert(USD.getCurrencyCode(), currencyCode.getCurrencyCode(),
            new BigDecimal(currencyCode.getRateToDollar()));
      }
    }
  }

}