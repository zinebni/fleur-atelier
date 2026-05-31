package com.fleuratelier.fleur_atelier_api.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fleuratelier.fleur_atelier_api.domain.model.Livraison;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    Optional<Livraison> findByCommandeId(Long commandeId);
}
