package org.infogain.domain.transaction.repository;

import org.infogain.domain.transaction.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Transaction saveTransaction(Transaction transaction);
    Optional<Transaction> getTransaction(String transactionId);
}
