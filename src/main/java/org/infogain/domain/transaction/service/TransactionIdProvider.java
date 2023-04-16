package org.infogain.domain.transaction.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionIdProvider {

    public String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}
