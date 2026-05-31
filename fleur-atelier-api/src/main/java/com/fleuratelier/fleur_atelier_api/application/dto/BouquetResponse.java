// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/BouquetResponse.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BouquetResponse {
    private final Long id;
    private final BigDecimal prixTotal;
    private final List<LigneBouquetResponse> lignes;
}
