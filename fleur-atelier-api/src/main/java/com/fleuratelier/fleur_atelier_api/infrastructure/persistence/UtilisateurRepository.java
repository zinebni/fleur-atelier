// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/persistence/UtilisateurRepository.java
package com.fleuratelier.fleur_atelier_api.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fleuratelier.fleur_atelier_api.domain.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);
}
