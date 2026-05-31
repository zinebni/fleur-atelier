// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/LigneBouquetResponse.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LigneBouquetResponse {
    private final Long id;
    private final Long articleId;
    private final String articleNom;
    private final BigDecimal articlePrix;
    private final Integer quantite;
    private final boolean disponible;
    private final String articleImageUrl;
}
