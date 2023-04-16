package org.infogain.domain.transaction.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Transaction {
    String transactionId;
    String userId;
    double amount;
}
