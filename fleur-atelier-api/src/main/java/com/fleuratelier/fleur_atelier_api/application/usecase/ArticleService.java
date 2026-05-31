// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/usecase/ArticleService.java
package com.fleuratelier.fleur_atelier_api.application.usecase;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fleuratelier.fleur_atelier_api.application.dto.ArticleResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.CreateArticleRequest;
import com.fleuratelier.fleur_atelier_api.application.dto.UpdateArticleRequest;
import com.fleuratelier.fleur_atelier_api.application.mapper.ArticleMapper;
import com.fleuratelier.fleur_atelier_api.domain.model.Article;
import com.fleuratelier.fleur_atelier_api.domain.model.Categorie;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.ArticleRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.CategorieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategorieRepository categorieRepository;
    private final ArticleMapper articleMapper;

    @Transactional(readOnly = true)
    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleResponse getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));
        return articleMapper.toResponse(article);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> searchByName(String query) {
        return articleRepository.findByNomContainingIgnoreCase(query).stream()
                .map(articleMapper::toResponse)
                .toList();
    }

        @Transactional
        public ArticleResponse createArticle(CreateArticleRequest request) {
        Categorie categorie = categorieRepository.findById(request.getCategorieId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categorie not found"));

            Article article = articleMapper.toEntity(request, categorie);

        Article saved = articleRepository.save(article);
            return articleMapper.toResponse(saved);
        }

        @Transactional
        public ArticleResponse updateArticle(Long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        Categorie categorie = categorieRepository.findById(request.getCategorieId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categorie not found"));

        articleMapper.updateEntity(article, request, categorie);

        Article saved = articleRepository.save(article);
        return articleMapper.toResponse(saved);
        }

        @Transactional
        public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));
        articleRepository.delete(article);
        }
}
