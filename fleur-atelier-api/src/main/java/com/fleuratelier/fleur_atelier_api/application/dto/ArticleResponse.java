// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/ArticleResponse.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleResponse {
    private final Long id;
    private final String nom;
    private final String description;
    private final BigDecimal prix;
    private final Integer stock;
    private final boolean disponible;
    private final String imageUrl;
    private final Long categorieId;
    private final String categorieNom;
    private final String categorieEmoji;
}
