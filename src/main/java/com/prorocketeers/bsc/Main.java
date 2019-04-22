package com.prorocketeers.bsc;

import com.google.inject.Guice;
import com.prorocketeers.bsc.exceptions.TransactionParseException;
import com.prorocketeers.bsc.models.Transaction;
import com.prorocketeers.bsc.repositories.ExchangeRateRepository;
import com.prorocketeers.bsc.repositories.TransactionsRepository;
import com.prorocketeers.bsc.utils.InputFileUtility;
import lombok.extern.java.Log;
import lombok.val;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Log
public class Main {

    public static void main(String[] args) throws TransactionParseException, IOException {
        // Dependency injections
        val injector = Guice.createInjector(new BSCModule());
        val transactionsRepository = injector.getInstance(TransactionsRepository.class);
        val exchangeRateRepository = injector.getInstance(ExchangeRateRepository.class);

        // Arguments handling
        if (args.length > 0) {
            val filePath = args[0];
            val loadedCurrencies = InputFileUtility.loadFileContentToTransactions(filePath);
            loadedCurrencies.forEach(transactionsRepository::save);
        }

        // Hardcoded exchange rates
        exchangeRateRepository.upsert("USD", "HKD", new BigDecimal("7.84"));
        exchangeRateRepository.upsert("USD", "RMB", new BigDecimal("6.69"));
        exchangeRateRepository.upsert("USD", "CZK", new BigDecimal("21.78"));
        exchangeRateRepository.upsert("USD", "EUR", new BigDecimal("1.18"));

        // Input handling
        val scannerTimer = new Timer();
        val scannerPeriod = 1000;
        System.out.println("Input you transaction:");
        scannerTimer.schedule(new TimerTask() {
            public void run() {
                val scanner = new Scanner(System.in);
                if (!scanner.hasNext()) {
                    return;
                }
                val line = scanner.nextLine();
                if (line.equals("quit")) {
                    System.out.println("=========== User terminated program ===========");
                    System.exit(0);
                }
                try {
                    Transaction.validateInput(line);
                    val record = Transaction.fromInput(line);
                    System.out.println("You added transaction: " + record.toOutput());
                    transactionsRepository.save(record);
                } catch (TransactionParseException ex) {
                    System.out.println("Transaction failed!");
                    System.out.println(ex.getMessage());
                    System.out.println("Do you wnat to quit? (If so write \"quit\")");
                    val quitline = scanner.nextLine();
                    if (quitline.equals("quit")) {
                        System.out.println("=========== User terminated program ===========");
                        System.exit(0);
                    }
                    else {
                        System.out.println("Not quiting then!");
                        System.out.println("Input you transaction:A");
                    }
                }
            }
        }, 0, scannerPeriod);

        // Output handling regular transaction report
        val outputTimer = new Timer();
        val outputPeriod = 10000; //One minute in ms
        outputTimer.schedule(new TimerTask() {
            public void run() {
            final var output = transactionsRepository
                    .getStatus()
                    .stream()
                    .filter(transaction -> transaction.amount.intValue()!=0) //  0 transactions not display
                    .map(transaction -> {
                        val exchangeCode = "USD";
                        val rate = exchangeRateRepository.get(transaction.currencyCode, exchangeCode);
                        return transaction.toOutput(rate.orElse(null), exchangeCode);
                    })
                    .collect(Collectors.joining(System.lineSeparator()));
            if(output.length()>0){
                System.out.println("\n==========================");
                System.out.println("Regular transaction report");
                System.out.println("==========================");
                System.out.println(output);
                System.out.println("========== END ===========\n");
            }
            }
        }, 0, outputPeriod);
    }
}
