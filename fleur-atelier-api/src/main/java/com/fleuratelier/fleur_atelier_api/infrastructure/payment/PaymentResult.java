// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/payment/PaymentResult.java
package com.fleuratelier.fleur_atelier_api.infrastructure.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResult {
    private final boolean success;
    private final String transactionId;
    private final String message;
}
