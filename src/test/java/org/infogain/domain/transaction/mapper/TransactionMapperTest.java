package org.infogain.domain.transaction.mapper;

import org.infogain.application.request.TransactionRequest;
import org.infogain.application.response.TransactionResponse;
import org.infogain.domain.transaction.model.Transaction;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {

    private static final String USER_ID = "userId";
    private static final double AMOUNT = 100.0;
    private static final String TRANSACTION_ID = "transactionId";


    private final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);

    @Test
    void should_toDomain_mapObjectCorrectly() {
        // given
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .userId(USER_ID)
                .amount(AMOUNT)
                .build();

        // when
        Transaction actualTransaction = transactionMapper.toDomain(transactionRequest);

        // then
        assertThat(actualTransaction.getTransactionId()).isNull();
        assertThat(actualTransaction.getUserId()).isEqualTo(USER_ID);
        assertThat(actualTransaction.getAmount()).isEqualTo(AMOUNT);
    }

    @Test
    void should_toDomain_mapObjectCorrectlyWithTransactionId() {
        // given
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .userId(USER_ID)
                .amount(AMOUNT)
                .build();

        // when
        Transaction actualTransaction = transactionMapper.toDomain(TRANSACTION_ID, transactionRequest);

        // then
        assertThat(actualTransaction.getTransactionId()).isEqualTo(TRANSACTION_ID);
        assertThat(actualTransaction.getUserId()).isEqualTo(USER_ID);
        assertThat(actualTransaction.getAmount()).isEqualTo(AMOUNT);
    }

    @Test
    void should_toApi_mapObjectCorrectly() {
        // given
        Transaction transaction = Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(AMOUNT)
                .build();

        // when
        TransactionResponse actualTransactionResponse = transactionMapper.toApi(transaction);

        // then
        assertThat(actualTransactionResponse.getTransactionId()).isEqualTo(TRANSACTION_ID);
        assertThat(actualTransactionResponse.getUserId()).isEqualTo(USER_ID);
        assertThat(actualTransactionResponse.getAmount()).isEqualTo(AMOUNT);
    }
}