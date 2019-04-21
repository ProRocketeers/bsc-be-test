package com.prorocketeers.bsc.repositories;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryExchangeRateRepository implements ExchangeRateRepository {

    private final Map<String, BigDecimal> data = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override public Optional<BigDecimal> get(@NotNull String from, @NotNull String to) {
        val rate = data.get(from + "-" + to);
        if(rate != null) {
            return Optional.of(rate);
        }
        val oppositeRate = data.get(to + "-" + from);
        if(oppositeRate != null) {
            return Optional.of(BigDecimal.ONE.divide(oppositeRate, 2, RoundingMode.HALF_UP));
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     * <br/>
     * There is no need to insert opposite exchange rate for the same pair of currencies, e.g. USD-EUR and EUR-USD.
     */
    @Override public void upsert(@NotNull String from, @NotNull String to, @NotNull BigDecimal rate) {
        val key = from + "-" + to;
        data.put(key, rate);
    }

}
