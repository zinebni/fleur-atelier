// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/persistence/LigneBouquetRepository.java
package com.fleuratelier.fleur_atelier_api.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fleuratelier.fleur_atelier_api.domain.model.LigneBouquet;

@Repository
public interface LigneBouquetRepository extends JpaRepository<LigneBouquet, Long> {
    Optional<LigneBouquet> findByBouquetIdAndArticleId(Long bouquetId, Long articleId);
    Optional<LigneBouquet> findByIdAndBouquetUtilisateurId(Long id, Long utilisateurId);
}
