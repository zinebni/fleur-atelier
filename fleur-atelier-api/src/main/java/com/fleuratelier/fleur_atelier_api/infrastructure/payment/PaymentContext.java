// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/payment/PaymentContext.java
package com.fleuratelier.fleur_atelier_api.infrastructure.payment;

import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fleuratelier.fleur_atelier_api.domain.model.Commande;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentContext {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentResult pay(String mode, Commande commande) {
        if (mode == null || mode.isBlank()) {
            throw new IllegalArgumentException("Invalid payment mode");
        }

        String key = mode.toLowerCase(Locale.ROOT);
        PaymentStrategy strategy = strategies.get(key);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid payment mode");
        }

        return strategy.process(commande);
    }
}
