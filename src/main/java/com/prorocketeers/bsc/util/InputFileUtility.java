package com.prorocketeers.bsc.util;

import com.prorocketeers.bsc.exception.TransactionParseException;
import com.prorocketeers.bsc.model.Transaction;
import lombok.val;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class InputFileUtility {

  /**
   * Creates a list of Transaction models from a file located on path.
   *
   * @param path path to a file with transactions
   * @return List of Transactions
   * @throws IOException When file is not found/readable/etc.
   * @throws TransactionParseException When data found in the file located on this path is not in valid format.
   */
  public static List<Transaction> loadFileContentToTransactions(String path)
      throws IOException, TransactionParseException {
    try (val linesStream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
      val lines = linesStream.collect(Collectors.toUnmodifiableList());
      for (val line : lines) {
        Transaction.validateInput(line);
      }
      return lines
          .stream()
          .map(Transaction::fromInput)
          .collect(Collectors.toUnmodifiableList());
    }
  }

}