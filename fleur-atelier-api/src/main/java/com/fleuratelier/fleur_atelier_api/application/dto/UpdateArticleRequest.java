// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/UpdateArticleRequest.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {

    @NotBlank
    @Size(max = 200)
    private String nom;

    @Size(max = 2000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal prix;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    private Boolean disponible;

    @Size(max = 1000)
    private String imageUrl;

    @NotNull
    @Positive
    private Long categorieId;
}
