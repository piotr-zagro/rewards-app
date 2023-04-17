package org.infogain.application.rest;

import lombok.RequiredArgsConstructor;
import org.infogain.application.request.TransactionRequest;
import org.infogain.application.response.TransactionResponse;
import org.infogain.domain.transaction.mapper.TransactionMapper;
import org.infogain.domain.transaction.model.Transaction;
import org.infogain.domain.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.TRANSACTION_PATH)
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.putTransaction(transactionMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionMapper.toApi(transaction));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable String transactionId, @RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.putTransaction(transactionMapper.toDomain(transactionId, request));
        return ResponseEntity.ok(transactionMapper.toApi(transaction));
    }
}
