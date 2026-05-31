// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/payment/PaypalPayment.java
package com.fleuratelier.fleur_atelier_api.infrastructure.payment;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.fleuratelier.fleur_atelier_api.domain.model.Commande;

@Service("paypal")
public class PaypalPayment implements PaymentStrategy {

    private static final double SUCCESS_RATE = 0.95;

    @Override
    public PaymentResult process(Commande commande) {
        boolean success = ThreadLocalRandom.current().nextDouble() < SUCCESS_RATE;
        String transactionId = success ? UUID.randomUUID().toString() : null;
        String message = success ? "PayPal payment approved" : "PayPal payment declined";
        return new PaymentResult(success, transactionId, message);
    }
}
