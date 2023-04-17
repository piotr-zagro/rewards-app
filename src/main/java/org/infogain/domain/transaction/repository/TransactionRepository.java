package org.infogain.domain.transaction.repository;

import org.infogain.domain.transaction.model.Transaction;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction saveTransaction(Transaction transaction);
    Optional<Transaction> getTransaction(String transactionId);
    List<Transaction> getTransactions(String userId, ZonedDateTime fromDate);
}
