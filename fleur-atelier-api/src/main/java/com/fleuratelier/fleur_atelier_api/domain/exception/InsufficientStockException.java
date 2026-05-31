// File: src/main/java/com/fleuratelier/fleur_atelier_api/domain/exception/InsufficientStockException.java
package com.fleuratelier.fleur_atelier_api.domain.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
