// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/LivraisonController.java
package com.fleuratelier.fleur_atelier_api.web;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fleuratelier.fleur_atelier_api.application.dto.LivraisonResponse;
import com.fleuratelier.fleur_atelier_api.application.mapper.LivraisonMapper;
import com.fleuratelier.fleur_atelier_api.domain.enums.StatutLivraison;
import com.fleuratelier.fleur_atelier_api.domain.model.Livraison;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.LivraisonRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.security.UtilisateurPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/livraisons")
@RequiredArgsConstructor
public class LivraisonController {

    private final LivraisonRepository livraisonRepository;
    private final LivraisonMapper livraisonMapper;

    /** User: get the delivery info for one of their orders */
    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<LivraisonResponse> getByCommande(
            @PathVariable Long commandeId,
            @AuthenticationPrincipal UtilisateurPrincipal principal
    ) {
        Livraison livraison = livraisonRepository.findByCommandeId(commandeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livraison not found"));

        // Ownership guard — the commande's owner OR admin
        Long ownerId = livraison.getCommande().getUtilisateur() != null
                ? livraison.getCommande().getUtilisateur().getId() : null;
        boolean isAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && (ownerId == null || !ownerId.equals(principal.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return ResponseEntity.ok(livraisonMapper.toResponse(livraison));
    }

    /** Admin: update delivery status */
    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LivraisonResponse> updateStatut(
            @PathVariable Long id,
            @RequestParam String statut
    ) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livraison not found"));

        try {
            StatutLivraison newStatut = StatutLivraison.valueOf(statut.toUpperCase());
            livraison.setStatut(newStatut);
            if (newStatut == StatutLivraison.LIVREE) {
                livraison.setDateLivraison(Instant.now());
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid statut: " + statut);
        }

        return ResponseEntity.ok(livraisonMapper.toResponse(livraisonRepository.save(livraison)));
    }

    /** Admin: list all deliveries */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<java.util.List<LivraisonResponse>> getAllLivraisons() {
        return ResponseEntity.ok(
                livraisonRepository.findAll().stream()
                        .map(livraisonMapper::toResponse)
                        .toList()
        );
    }
}
