package org.infogain.domain.transaction.service;

import org.infogain.domain.transaction.model.Transaction;

import java.util.Optional;

public interface TransactionService {
    Transaction putTransaction(Transaction transaction);
}
