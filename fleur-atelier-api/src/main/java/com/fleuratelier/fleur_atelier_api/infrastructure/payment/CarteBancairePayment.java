// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/payment/CarteBancairePayment.java
package com.fleuratelier.fleur_atelier_api.infrastructure.payment;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.fleuratelier.fleur_atelier_api.domain.model.Commande;

@Service("carte")
public class CarteBancairePayment implements PaymentStrategy {

    private static final double SUCCESS_RATE = 0.90;

    @Override
    public PaymentResult process(Commande commande) {
        boolean success = ThreadLocalRandom.current().nextDouble() < SUCCESS_RATE;
        String transactionId = success ? UUID.randomUUID().toString() : null;
        String message = success ? "Card payment approved" : "Card payment declined";
        return new PaymentResult(success, transactionId, message);
    }
}
