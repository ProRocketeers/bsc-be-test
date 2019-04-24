package com.prorocketeers.bsc.task;

import com.prorocketeers.bsc.exception.TransactionParseException;
import com.prorocketeers.bsc.model.Transaction;
import com.prorocketeers.bsc.repository.TransactionsRepository;
import java.util.Scanner;
import java.util.TimerTask;
import lombok.val;

/**
 * @author - zazodan
 */
public class InputHandlerTimerTask extends TimerTask {

  private static final String COMMAND_QUIT = "quit";

  private TransactionsRepository transactionsRepository;

  public InputHandlerTimerTask(TransactionsRepository transactionsRepository) {
    this.transactionsRepository = transactionsRepository;
  }

  @Override
  public void run() {
    val scanner = new Scanner(System.in);
    if (!scanner.hasNext()) {
      return;
    }

    val line = scanner.nextLine();
    if (line.equals(COMMAND_QUIT)) {
      turnOffApplication();
    }

    try {
      Transaction.validateInput(line);
      val record = Transaction.fromInput(line);
      System.out.println("You added transaction: " + record.toOutput());
      transactionsRepository.save(record);
    } catch (TransactionParseException ex) {
      System.out.println("Transaction failed!");
      System.out.println(ex.getMessage());
      System.out.println("Do you want to quit? (If so write \"quit\")");

      val quitLine = scanner.nextLine();
      if (quitLine.equals(COMMAND_QUIT)) {
        turnOffApplication();
      } else {
        System.out.println("Not quiting then!");
        System.out.println("Input you transaction:A");
      }
    }
  }

  private void turnOffApplication() {
    System.out.println("=========== User terminated program ===========");
    System.exit(0);
  }

}