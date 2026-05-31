// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/LigneCommandeResponse.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LigneCommandeResponse {
    private final Long id;
    private final Long articleId;
    private final String nomArticle;
    private final BigDecimal prixUnitaire;
    private final Integer quantite;
    private final BigDecimal sousTotal;
}
