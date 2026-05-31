// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/CategorieResponse.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategorieResponse {
    private final Long id;
    private final String nom;
    private final String description;
    private final String emoji;
}
