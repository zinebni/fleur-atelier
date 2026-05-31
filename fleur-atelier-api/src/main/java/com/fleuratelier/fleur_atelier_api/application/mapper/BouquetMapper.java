// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/mapper/BouquetMapper.java
package com.fleuratelier.fleur_atelier_api.application.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fleuratelier.fleur_atelier_api.application.dto.BouquetResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.LigneBouquetResponse;
import com.fleuratelier.fleur_atelier_api.domain.model.Article;
import com.fleuratelier.fleur_atelier_api.domain.model.Bouquet;
import com.fleuratelier.fleur_atelier_api.domain.model.LigneBouquet;

@Component
public class BouquetMapper {

    public BouquetResponse toResponse(Bouquet bouquet) {
        List<LigneBouquetResponse> lignes = bouquet.getLignes().stream()
                .map(this::toResponse)
                .toList();
        return new BouquetResponse(bouquet.getId(), bouquet.getPrixTotal(), lignes);
    }

    public LigneBouquetResponse toResponse(LigneBouquet ligne) {
        Article article = ligne.getArticle();
        return new LigneBouquetResponse(
                ligne.getId(),
                article.getId(),
                article.getNom(),
                article.getPrix(),
                ligne.getQuantite(),
                article.isDisponible(),
                article.getImageUrl()
        );
    }
}
