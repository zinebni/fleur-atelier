// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/PaiementRequest.java
package com.fleuratelier.fleur_atelier_api.application.dto;

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
public class PaiementRequest {

    @NotNull
    @Positive
    private Long commandeId;

    @NotBlank
    @Size(max = 50)
    private String modePaiement;

    // ── Delivery address (required at payment time) ──────────────────────────
    @NotBlank
    private String adresse;

    @NotBlank
    private String ville;

    @NotBlank
    private String codePostal;

    @NotBlank
    private String telephone;

    private String pays = "Maroc";
}
