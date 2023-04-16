package org.infogain.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.infogain.domain.reward.service.RewardService;
import org.infogain.domain.transaction.exception.TransactionNotFoundException;
import org.infogain.domain.transaction.model.Transaction;
import org.infogain.domain.transaction.repository.TransactionRepository;
import org.infogain.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final RewardService rewardService;
    private final TransactionIdProvider transactionIdProvider;

    @Override
    public Transaction putTransaction(Transaction transaction) {
        userService.validateUserExists(transaction.getUserId());
        validateTransactionForEdits(transaction);

        removePointsFromOldTransaction(transaction);

        addPointsFromTransaction(transaction);

        return transactionRepository.saveTransaction(ensureTransactionId(transaction));
    }

    private void removePointsFromOldTransaction(Transaction transaction) {
        if (Objects.isNull(transaction.getTransactionId())) {
            return;
        }

        Transaction oldTransaction = transactionRepository.getTransaction(transaction.getTransactionId()).get();
        int pointsToRemove = rewardService.calculatePoints(oldTransaction.getAmount());
        rewardService.addPointsToUser(oldTransaction.getUserId(), -pointsToRemove);
    }

    private void addPointsFromTransaction(Transaction transaction) {
        int pointsToAdd = rewardService.calculatePoints(transaction.getAmount());
        rewardService.addPointsToUser(transaction.getUserId(), pointsToAdd);
    }

    private Transaction ensureTransactionId(Transaction transaction) {
        if (Objects.nonNull(transaction.getTransactionId())) {
            return transaction;
        }
        return transaction.toBuilder()
                .transactionId(transactionIdProvider.generateTransactionId())
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
