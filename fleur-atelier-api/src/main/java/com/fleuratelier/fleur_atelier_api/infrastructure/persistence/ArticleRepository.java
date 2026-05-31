// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/persistence/ArticleRepository.java
package com.fleuratelier.fleur_atelier_api.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fleuratelier.fleur_atelier_api.domain.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByNomContainingIgnoreCase(String nom);
}
