package com.prorocketeers.bsc.models;

import com.prorocketeers.bsc.exceptions.TransactionParseException;
import lombok.Data;
import lombok.extern.java.Log;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

/**
 * Representation of exactly one positive or negative transaction in the system.
 *
 */
@Data
@Log
public class Transaction {

    private static final int CODE_INDEX = 0;
    private static final int AMOUNT_INDEX = 1;
    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final int EXACT_LINE_WORD_COUNT = 2;

    /**
     * May be any uppercase 3 letter code, such as USD, HKD, RMB, NZD, GBP etc.
     */
    public final String currencyCode;
    public final BigDecimal amount;

    /**
     * Validates string input.
     *
     * @param line one line containing one transaction in a required format e.g. USD 300
     * @throws TransactionParseException When input is invalid.
     */
    public static void validateInput(@NotNull String line) throws TransactionParseException {
        val data = line.split(" ");
        val firstThreeUppercaseLettersPattern = Pattern.compile("[A-Z]{3}");
        val onlyDigitsPattern = Pattern.compile("^([+-]?\\d*\\.?\\d*)$");
        if (data.length != EXACT_LINE_WORD_COUNT) {
            throw new TransactionParseException("Invalid data format in line. " + line);
        } else if (data[CODE_INDEX].length() != CURRENCY_CODE_LENGTH) {
            throw new TransactionParseException("Currency symbol length must be 3. " + data[CODE_INDEX]);
        } else if (!firstThreeUppercaseLettersPattern.matcher(data[CODE_INDEX]).matches()) {
            throw new TransactionParseException("Currency is specified in wrong format, must only uppercase letters. " + data[CODE_INDEX]);
        }  else if (!onlyDigitsPattern.matcher(data[AMOUNT_INDEX]).matches()) {
            throw new TransactionParseException("Amount must be specified only as float number. " + data[AMOUNT_INDEX]);
        }
    }

    /**
     * Parses string input to Transaction model.
     * <br/>
     * It does not validate the input.
     *
     * @param line one line containing one transaction in a required format e.g. USD 300
     * @return Transaction model with data parsed from input line.
     * @see #validateInput(String)
     */
    public static @NotNull Transaction fromInput(@NotNull String line) {
        val data = line.split(" ");
        val currencyCode = data[CODE_INDEX].toUpperCase();
        val amount = data[AMOUNT_INDEX].strip();
        return new Transaction(currencyCode, new BigDecimal(amount));
    }

    /**
     * Add this and input amount.
     * <br/>
     * It does not change this instance, rather returns new Transaction instance with added value.
     *
     * @param currency currency to be added to this
     * @return New instance with this currencyCode added value.
     */
    public Transaction add(Transaction currency) {
        return new Transaction(currencyCode, amount.add(currency.amount));
    }

    /**
     * Serializes this to a string.
     * When exchange rate is not specified, it matches the input format used in {@link Transaction#fromInput(String)}
     *
     * @param exchangeRate rate, may be null, calculated and included in output
     * @param exchangeCode {@link Transaction#currencyCode}
     * @return Serialized string including value in exchange currency code if specified
     */
    public String toOutput(@Nullable BigDecimal exchangeRate, @Nullable String exchangeCode) {
        val usdAmount = exchangeRate != null
            ? " (" + (this.amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP)) + " " + exchangeCode + ")"
            : "";
        return this.currencyCode + " " + this.amount.setScale(0, RoundingMode.HALF_UP) + usdAmount;
    }
    /**
     * Serializes this to a string.
     * It matches the input format used in {@link Transaction#fromInput(String)}
     *
     * @return Serialized string
     */
    public String toOutput() {
        return this.toOutput(null, null);
    }

}
