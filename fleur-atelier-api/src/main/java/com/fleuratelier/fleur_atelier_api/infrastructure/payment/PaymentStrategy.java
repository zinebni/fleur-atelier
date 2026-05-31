// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/payment/PaymentStrategy.java
package com.fleuratelier.fleur_atelier_api.infrastructure.payment;

import com.fleuratelier.fleur_atelier_api.domain.model.Commande;

public interface PaymentStrategy {
    PaymentResult process(Commande commande);
}
