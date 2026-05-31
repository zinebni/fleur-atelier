// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/LivraisonResponse.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LivraisonResponse {
    private final Long id;
    private final Long commandeId;       // ← new
    private final String adresse;
    private final String ville;
    private final String codePostal;
    private final String telephone;
    private final String pays;
    private final BigDecimal fraisLivraison;
    private final String statut;
    private final Instant dateCreation;
    private final Instant dateLivraison;
}
