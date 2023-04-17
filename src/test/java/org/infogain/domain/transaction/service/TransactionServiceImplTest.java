package org.infogain.domain.transaction.service;

import org.infogain.domain.transaction.exception.TransactionNotFoundException;
import org.infogain.domain.transaction.model.Transaction;
import org.infogain.domain.transaction.repository.TransactionRepository;
import org.infogain.domain.user.service.UserService;
import org.infogain.domain.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
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
    private TransactionIdProvider transactionIdProvider;
    @Mock
    private DateTimeUtil dateTimeUtil;

    private TransactionServiceImpl transactionServiceImpl;

    @BeforeEach
    void setUp() {
        transactionServiceImpl = new TransactionServiceImpl(transactionRepository, userService, transactionIdProvider, dateTimeUtil);
    }

    @Test
    void should_putTransaction_addNewTransaction() {
        // given
        final double amountToSave = 100.0;
        ZonedDateTime fixedNow = ZonedDateTime.of(2023, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

        Transaction inputTransaction = Transaction.builder()
                .transactionId(null)
                .userId(USER_ID)
                .amount(amountToSave)
                .build();
        Transaction transactionToSave = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(amountToSave)
                .createdAt(fixedNow)
                .build();

        when(dateTimeUtil.now()).thenReturn(fixedNow);
        when(transactionIdProvider.generateTransactionId()).thenReturn(TRANSACTION_ID);
        when(transactionRepository.saveTransaction(transactionToSave)).thenReturn(transactionToSave);


        // when
        Transaction actualTransaction = transactionServiceImpl.putTransaction(inputTransaction);

        // then
        verify(userService, times(1)).validateUserExists(USER_ID);
        verify(transactionRepository, never()).getTransaction(anyString());

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
    void should_putTransaction_editTransaction() {
        // given
        final double oldAmount = 60.0;
        final double amountToSave = 100.0;
        final String differentUserId = "differentUserId";
        ZonedDateTime fixedNow = ZonedDateTime.of(2023, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

        Transaction existingTransaction = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(differentUserId)
                .amount(oldAmount)
                .createdAt(fixedNow)
                .build();
        Transaction transactionToSave = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(amountToSave)
                .createdAt(fixedNow)
                .build();

        when(transactionRepository.getTransaction(TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.saveTransaction(transactionToSave)).thenReturn(transactionToSave);

        // when
        Transaction actualTransaction = transactionServiceImpl.putTransaction(transactionToSave);

        // then
        verify(userService, times(1)).validateUserExists(USER_ID);

        assertThat(actualTransaction).isEqualTo(transactionToSave);
    }

    @Test
    void should_getTransactionsForUser_returnTransactionsForUser() {
        // given
        final double amountToSave = 100.0;
        ZonedDateTime fromDate = ZonedDateTime.of(2023, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

        Transaction transaction1 = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(amountToSave)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(amountToSave)
                .build();

        when(transactionRepository.getTransactions(USER_ID, fromDate)).thenReturn(List.of(transaction1, transaction2));

        // when
        List<Transaction> actualTransactions = transactionServiceImpl.getTransactionsForUser(USER_ID, fromDate);

        // then
        verify(userService, times(1)).validateUserExists(USER_ID);

        assertThat(actualTransactions).containsExactly(transaction1, transaction2);
    }
}