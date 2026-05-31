// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/CommandeResponse.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.fleuratelier.fleur_atelier_api.domain.enums.StatutCommande;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommandeResponse {
    private final Long id;
    private final BigDecimal montantTotal;
    private final StatutCommande statut;
    private final Instant dateCommande;
    private final Instant datePaiement;
    private final String modePaiement;
    private final List<LigneCommandeResponse> lignes;
    private final LivraisonResponse livraison;
    // User info (for admin views)
    private final Long utilisateurId;
    private final String utilisateurPrenom;
    private final String utilisateurNom;
    private final String utilisateurEmail;
}
