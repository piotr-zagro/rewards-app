package org.infogain.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.infogain.domain.transaction.exception.TransactionNotFoundException;
import org.infogain.domain.transaction.model.Transaction;
import org.infogain.domain.transaction.repository.TransactionRepository;
import org.infogain.domain.user.service.UserService;
import org.infogain.domain.util.DateTimeUtil;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionIdProvider transactionIdProvider;
    private final DateTimeUtil dateTimeUtil;

    @Override
    public Transaction putTransaction(Transaction transaction) {
        userService.validateUserExists(transaction.getUserId());
        validateTransactionForEdits(transaction);

        return transactionRepository.saveTransaction(enrichData(transaction));
    }

    @Override
    public List<Transaction> getTransactionsForUser(String userId, ZonedDateTime fromDate) {
        userService.validateUserExists(userId);
        return transactionRepository.getTransactions(userId, fromDate);
    }

    private Transaction enrichData(Transaction transaction) {
        if (Objects.nonNull(transaction.getTransactionId())) {
            return transaction;
        }
        return transaction.toBuilder()
                .transactionId(transactionIdProvider.generateTransactionId())
                .createdAt(dateTimeUtil.now())
                .build();
    }

    private void validateTransactionForEdits(Transaction transaction) {
        if (Objects.isNull(transaction.getTransactionId())) {
            return;
        }

        Optional<Transaction> transactionOptional = transactionRepository.getTransaction(transaction.getTransactionId());

        if (transactionOptional.isEmpty()) {
            throw new TransactionNotFoundException(transaction.getTransactionId());
        }
    }

}
