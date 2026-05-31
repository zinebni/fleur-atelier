// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/persistence/CategorieRepository.java
package com.fleuratelier.fleur_atelier_api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fleuratelier.fleur_atelier_api.domain.model.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
}
