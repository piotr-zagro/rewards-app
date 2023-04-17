package org.infogain.infrastructure.repository;

import org.infogain.domain.transaction.model.Transaction;
import org.infogain.domain.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TransactionRepositoryInMemoryImpl implements TransactionRepository {

    private final Map<String, Transaction> memory = new HashMap<>();

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        memory.put(transaction.getTransactionId(), transaction);
        return getTransaction(transaction.getTransactionId()).get();
    }

    @Override
    public Optional<Transaction> getTransaction(String transactionId) {
        return Optional.ofNullable(memory.getOrDefault(transactionId, null));
    }

    @Override
    public List<Transaction> getTransactions(String userId, ZonedDateTime fromDate) {
        return memory.values().stream()
                .filter(transaction -> transaction.getUserId().equals(userId) && transaction.getCreatedAt().isAfter(fromDate))
                .collect(Collectors.toList());
    }
}
