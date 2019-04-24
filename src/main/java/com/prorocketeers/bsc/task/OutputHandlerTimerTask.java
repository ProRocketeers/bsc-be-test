package com.prorocketeers.bsc.task;

import static com.prorocketeers.bsc.model.CurrencyCode.*;

import com.prorocketeers.bsc.repository.ExchangeRateRepository;
import com.prorocketeers.bsc.repository.TransactionsRepository;
import java.util.TimerTask;
import java.util.stream.Collectors;
import lombok.val;

/**
 * @author - zazodan
 */
public class OutputHandlerTimerTask extends TimerTask {

  private TransactionsRepository transactionsRepository;
  private ExchangeRateRepository exchangeRateRepository;

  public OutputHandlerTimerTask(TransactionsRepository transactionsRepository, ExchangeRateRepository exchangeRateRepository) {
    this.transactionsRepository = transactionsRepository;
    this.exchangeRateRepository = exchangeRateRepository;
  }

  @Override
  public void run() {
    final var output = transactionsRepository
        .getStatus()
        .stream()
        .filter(
            transaction -> transaction.amount.intValue() != 0) //  0 transactions not display
        .map(transaction -> {
          val exchangeCode =  USD.getCurrencyCode();
          val rate = exchangeRateRepository.get(transaction.currencyCode, exchangeCode);
          return transaction.toOutput(rate.orElse(null), exchangeCode);
        })
        .collect(Collectors.joining(System.lineSeparator()));

    if (output.length() > 0) {
      System.out.println("\n==========================");
      System.out.println("Regular transaction report");
      System.out.println("==========================");
      System.out.println(output);
      System.out.println("========== END ===========\n");
    }
  }

}