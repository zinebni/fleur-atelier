// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/GlobalExceptionHandler.java
package com.fleuratelier.fleur_atelier_api.web;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fleuratelier.fleur_atelier_api.domain.exception.InsufficientStockException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        if (message.isBlank()) {
            message = "Validation failed";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(message, Instant.now()));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiError> handleInsufficientStock(InsufficientStockException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Insufficient stock";
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(message, Instant.now()));
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleOptimisticLock(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("Concurrent update detected. Please retry.", Instant.now()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Unexpected error";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(message, Instant.now()));
    }

    @Getter
    @AllArgsConstructor
    private static class ApiError {
        private final String message;
        private final Instant timestamp;
    }
}
