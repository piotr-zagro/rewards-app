package org.infogain.infrastructure.repository;

import org.infogain.domain.transaction.model.Transaction;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionRepositoryInMemoryImplTest {

    private TransactionRepositoryInMemoryImpl transactionRepositoryInMemoryImpl;

    @Test
    void should_saveTransaction_addNewEntry() {
        // given
        transactionRepositoryInMemoryImpl = new TransactionRepositoryInMemoryImpl();

        Transaction transactionToSave = Transaction.builder()
                .transactionId("trId")
                .userId("userId")
                .createdAt(ZonedDateTime.of(2023, 4, 20, 1, 1, 1, 1, ZoneOffset.UTC))
                .amount(100.0)
                .build();

        // when
        Transaction actualTransaction = transactionRepositoryInMemoryImpl.saveTransaction(transactionToSave);

        // then
        Transaction savedTransaction = transactionRepositoryInMemoryImpl.getTransaction(transactionToSave.getTransactionId()).get();

        assertThat(actualTransaction).isEqualTo(transactionToSave);
        assertThat(savedTransaction).isEqualTo(transactionToSave);

    }

    @Test
    void should_getTransactions_returnProperData() {
        // given
        transactionRepositoryInMemoryImpl = new TransactionRepositoryInMemoryImpl();

        final String userId = "userId";
        ZonedDateTime fixedDate = ZonedDateTime.of(2023, 4, 20, 1, 1, 1, 1, ZoneOffset.UTC);

        Transaction transaction1 = Transaction.builder()
                .transactionId("trId1")
                .userId(userId)
                .createdAt(fixedDate)
                .amount(100.0)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionId("trId2")
                .userId(userId)
                .createdAt(fixedDate.minusDays(1))
                .amount(10.0)
                .build();
        Transaction transaction3 = Transaction.builder()
                .transactionId("trId3")
                .userId("differentUserId")
                .createdAt(fixedDate.minusDays(3))
                .amount(90.0)
                .build();
        Transaction transaction4 = Transaction.builder()
                .transactionId("trId4")
                .userId(userId)
                .createdAt(fixedDate.minusYears(1))
                .amount(60.0)
                .build();

        transactionRepositoryInMemoryImpl.saveTransaction(transaction1);
        transactionRepositoryInMemoryImpl.saveTransaction(transaction2);
        transactionRepositoryInMemoryImpl.saveTransaction(transaction3);
        transactionRepositoryInMemoryImpl.saveTransaction(transaction4);

        // when
        List<Transaction> actualTransactions = transactionRepositoryInMemoryImpl.getTransactions(userId, fixedDate.minusMonths(3));

        // then
        assertThat(actualTransactions).contains(transaction1, transaction2);
    }
}