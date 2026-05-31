// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/persistence/CommandeRepository.java
package com.fleuratelier.fleur_atelier_api.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fleuratelier.fleur_atelier_api.domain.model.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    // User: own orders — lignes not needed in list view, user is needed
    List<Commande> findByUtilisateurIdOrderByDateCommandeDesc(Long utilisateurId);

    // User: single order detail — eagerly load lignes
    @EntityGraph(attributePaths = "lignes")
    Optional<Commande> findById(Long id);

    // Admin: all orders — eagerly load utilisateur, lignes, livraison to avoid LazyInit
    @Query("SELECT DISTINCT c FROM Commande c " +
           "LEFT JOIN FETCH c.utilisateur " +
           "LEFT JOIN FETCH c.lignes " +
           "LEFT JOIN FETCH c.livraison " +
           "ORDER BY c.dateCommande DESC")
    List<Commande> findAllWithDetails();
}
