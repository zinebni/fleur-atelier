// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/mapper/CategorieMapper.java
package com.fleuratelier.fleur_atelier_api.application.mapper;

import org.springframework.stereotype.Component;

import com.fleuratelier.fleur_atelier_api.application.dto.CategorieResponse;
import com.fleuratelier.fleur_atelier_api.domain.model.Categorie;

@Component
public class CategorieMapper {

    public CategorieResponse toResponse(Categorie categorie) {
        return new CategorieResponse(
                categorie.getId(),
                categorie.getNom(),
                categorie.getDescription(),
                categorie.getEmoji()
        );
    }
}
