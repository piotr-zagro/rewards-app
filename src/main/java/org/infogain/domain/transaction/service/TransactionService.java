package org.infogain.domain.transaction.service;

import org.infogain.domain.transaction.model.Transaction;

import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionService {
    Transaction putTransaction(Transaction transaction);

    List<Transaction> getTransactionsForUser(String userId, ZonedDateTime fromDate);
}
