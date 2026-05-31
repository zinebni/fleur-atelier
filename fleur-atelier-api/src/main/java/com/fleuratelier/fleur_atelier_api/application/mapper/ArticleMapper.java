// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/mapper/ArticleMapper.java
package com.fleuratelier.fleur_atelier_api.application.mapper;

import org.springframework.stereotype.Component;

import com.fleuratelier.fleur_atelier_api.application.dto.ArticleResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.CreateArticleRequest;
import com.fleuratelier.fleur_atelier_api.application.dto.UpdateArticleRequest;
import com.fleuratelier.fleur_atelier_api.domain.model.Article;
import com.fleuratelier.fleur_atelier_api.domain.model.Categorie;

@Component
public class ArticleMapper {

    public ArticleResponse toResponse(Article article) {
        Categorie categorie = article.getCategorie();
        Long categorieId = categorie != null ? categorie.getId() : null;
        String categorieNom = categorie != null ? categorie.getNom() : null;
        String categorieEmoji = categorie != null ? categorie.getEmoji() : null;

        return new ArticleResponse(
                article.getId(),
                article.getNom(),
                article.getDescription(),
                article.getPrix(),
                article.getStock(),
                article.isDisponible(),
                article.getImageUrl(),
                categorieId,
                categorieNom,
                categorieEmoji
        );
    }

    public Article toEntity(CreateArticleRequest request, Categorie categorie) {
        return Article.builder()
                .nom(request.getNom())
                .description(request.getDescription())
                .prix(request.getPrix())
                .stock(request.getStock())
                .disponible(resolveDisponibilite(request.getStock(), request.getDisponible()))
                .imageUrl(request.getImageUrl())
                .categorie(categorie)
                .build();
    }

    public void updateEntity(Article article, UpdateArticleRequest request, Categorie categorie) {
        article.setNom(request.getNom());
        article.setDescription(request.getDescription());
        article.setPrix(request.getPrix());
        article.setStock(request.getStock());
        article.setDisponible(resolveDisponibilite(request.getStock(), request.getDisponible()));
        article.setImageUrl(request.getImageUrl());
        article.setCategorie(categorie);
    }

    private boolean resolveDisponibilite(Integer stock, Boolean disponible) {
        if (stock != null && stock == 0) {
            return false;
        }
        return Boolean.TRUE.equals(disponible);
    }
}
