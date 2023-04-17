package org.infogain.domain.transaction.mapper;

import org.infogain.application.request.TransactionRequest;
import org.infogain.application.response.TransactionResponse;
import org.infogain.domain.transaction.model.Transaction;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {
    Transaction toDomain(TransactionRequest transactionRequest);

    Transaction toDomain(String transactionId, TransactionRequest transactionRequest);

    TransactionResponse toApi(Transaction transaction);
}
