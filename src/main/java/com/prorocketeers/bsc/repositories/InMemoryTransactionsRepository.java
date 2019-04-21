package com.prorocketeers.bsc.repositories;

import com.prorocketeers.bsc.models.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;


/**
 *
 */
public class InMemoryTransactionsRepository implements TransactionsRepository {

    private List<Transaction> data = new CopyOnWriteArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override public Collection<Transaction> getStatus() {
        return data
            .stream()
            .collect(groupingBy(Transaction::getCurrencyCode))
            .values()
            .stream()
            .map(currencies -> currencies.stream().reduce(Transaction::add))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toUnmodifiableList());
    }

    /**
     * {@inheritDoc}
     */
    @Override public void save(@NotNull Transaction transaction) {
        data.add(transaction);
    }
}
