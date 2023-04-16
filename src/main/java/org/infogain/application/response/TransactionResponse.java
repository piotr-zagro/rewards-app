package org.infogain.application.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionResponse {
    String transactionId;
    String userId;
    double amount;
}
