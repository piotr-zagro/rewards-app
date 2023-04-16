package org.infogain.domain.transaction.service;

import org.infogain.domain.reward.service.RewardService;
import org.infogain.domain.transaction.exception.TransactionNotFoundException;
import org.infogain.domain.transaction.model.Transaction;
import org.infogain.domain.transaction.repository.TransactionRepository;
import org.infogain.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private static final String USER_ID = "userId";
    private static final String TRANSACTION_ID = "transactionId";

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserService userService;
    @Mock
    private RewardService rewardService;
    @Mock
    private TransactionIdProvider transactionIdProvider;

    private TransactionServiceImpl transactionServiceImpl;

    @BeforeEach
    void setUp() {
        transactionServiceImpl = new TransactionServiceImpl(transactionRepository, userService, rewardService, transactionIdProvider);
    }

    @Test
    void should_putTransaction_addNewTransactionAndIncreasePoints() {
        // given
        final double amountToSave = 100.0;
        final int pointsToAdd = 50;

        Transaction inputTransaction = Transaction.builder()
                .transactionId(null)
                .userId(USER_ID)
                .amount(amountToSave)
                .build();
        Transaction transactionToSave = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(amountToSave)
                .build();

        when(rewardService.calculatePoints(amountToSave)).thenReturn(pointsToAdd);
        when(transactionIdProvider.generateTransactionId()).thenReturn(TRANSACTION_ID);
        when(transactionRepository.saveTransaction(transactionToSave)).thenReturn(transactionToSave);


        // when
        Transaction actualTransaction = transactionServiceImpl.putTransaction(inputTransaction);

        // then
        verify(userService, times(1)).validateUserExists(USER_ID);
        verify(transactionRepository, never()).getTransaction(anyString());
        verify(rewardService, times(1)).addPointsToUser(USER_ID, pointsToAdd);

        assertThat(actualTransaction).isEqualTo(transactionToSave);
    }

    @Test
    void should_putTransaction_throwExceptionIfTransactionNotFound() {
        // given
        final double amountToSave = 100.0;

        Transaction inputTransaction = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(amountToSave)
                .build();

        when(transactionRepository.getTransaction(TRANSACTION_ID)).thenReturn(Optional.empty());

        // when // then
        assertThrows(TransactionNotFoundException.class, () -> transactionServiceImpl.putTransaction(inputTransaction));

        verify(userService, times(1)).validateUserExists(USER_ID);
    }

    @Test
    void should_putTransaction_editTransactionAndEditPointsProperly() {
        // given
        final double oldAmount = 60.0;
        final double amountToSave = 100.0;
        final int pointsToRemove = 10;
        final int pointsToAdd = 50;
        final String differentUserId = "differentUserId";

        Transaction existingTransaction = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(differentUserId)
                .amount(oldAmount)
                .build();
        Transaction transactionToSave = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(amountToSave)
                .build();

        when(transactionRepository.getTransaction(TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
        when(rewardService.calculatePoints(oldAmount)).thenReturn(pointsToRemove);
        when(rewardService.calculatePoints(amountToSave)).thenReturn(pointsToAdd);
        when(transactionRepository.saveTransaction(transactionToSave)).thenReturn(transactionToSave);

        // when
        Transaction actualTransaction = transactionServiceImpl.putTransaction(transactionToSave);

        // then
        verify(userService, times(1)).validateUserExists(USER_ID);
        verify(rewardService, times(1)).addPointsToUser(USER_ID, pointsToAdd);
        verify(rewardService, times(1)).addPointsToUser(differentUserId, -pointsToRemove);

        assertThat(actualTransaction).isEqualTo(transactionToSave);
    }
}