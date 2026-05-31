// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/mapper/LivraisonMapper.java
package com.fleuratelier.fleur_atelier_api.application.mapper;

import org.springframework.stereotype.Component;

import com.fleuratelier.fleur_atelier_api.application.dto.LivraisonResponse;
import com.fleuratelier.fleur_atelier_api.domain.model.Livraison;

@Component
public class LivraisonMapper {

    public LivraisonResponse toResponse(Livraison livraison) {
        if (livraison == null) return null;
        Long commandeId = livraison.getCommande() != null ? livraison.getCommande().getId() : null;
        return new LivraisonResponse(
                livraison.getId(),
                commandeId,
                livraison.getAdresse(),
                livraison.getVille(),
                livraison.getCodePostal(),
                livraison.getTelephone(),
                livraison.getPays(),
                livraison.getFraisLivraison(),
                livraison.getStatut().name(),
                livraison.getDateCreation(),
                livraison.getDateLivraison()
        );
    }
}
