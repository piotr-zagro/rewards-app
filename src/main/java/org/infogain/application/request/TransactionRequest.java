package org.infogain.application.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionRequest {
    String userId;
    double amount;
}
