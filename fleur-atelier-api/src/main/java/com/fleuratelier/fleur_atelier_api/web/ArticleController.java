// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/ArticleController.java
package com.fleuratelier.fleur_atelier_api.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fleuratelier.fleur_atelier_api.application.dto.ArticleResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.CreateArticleRequest;
import com.fleuratelier.fleur_atelier_api.application.dto.UpdateArticleRequest;
import com.fleuratelier.fleur_atelier_api.application.usecase.ArticleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Validated
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleResponse>> search(@RequestParam("q") @NotBlank String query) {
        return ResponseEntity.ok(articleService.searchByName(query));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@Valid @RequestBody CreateArticleRequest request) {
        return ResponseEntity.ok(articleService.createArticle(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateArticleRequest request
    ) {
        return ResponseEntity.ok(articleService.updateArticle(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable @Positive Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
