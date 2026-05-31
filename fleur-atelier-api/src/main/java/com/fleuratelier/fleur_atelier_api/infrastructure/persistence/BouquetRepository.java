// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/persistence/BouquetRepository.java
package com.fleuratelier.fleur_atelier_api.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fleuratelier.fleur_atelier_api.domain.model.Bouquet;

@Repository
public interface BouquetRepository extends JpaRepository<Bouquet, Long> {

    @EntityGraph(attributePaths = {"lignes", "lignes.article"})
    Optional<Bouquet> findByUtilisateurId(Long utilisateurId);
}
