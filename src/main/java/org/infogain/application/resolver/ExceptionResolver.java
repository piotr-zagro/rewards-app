package org.infogain.application.resolver;

import lombok.extern.slf4j.Slf4j;
import org.infogain.application.response.ErrorResponse;
import org.infogain.domain.transaction.exception.TransactionNotFoundException;
import org.infogain.domain.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionResolver {

    private static final String TRANSACTION_NOT_FOUND_ERROR_MESSAGE = "Not found transactionId: %s";
    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "Not found userId: %s";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> transactionNotFoundException(TransactionNotFoundException exception) {
        log.info("Not found transactionId: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(String.format(TRANSACTION_NOT_FOUND_ERROR_MESSAGE, exception.getMessage())));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exception) {
        log.info("Not found userId: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(String.format(USER_NOT_FOUND_ERROR_MESSAGE, exception.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> uncaughtException(Exception exception) {
        log.info("Uncaught exception: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(INTERNAL_SERVER_ERROR));
    }
}
