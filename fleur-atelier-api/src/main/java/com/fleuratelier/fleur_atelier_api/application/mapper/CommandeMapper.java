// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/mapper/CommandeMapper.java
package com.fleuratelier.fleur_atelier_api.application.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fleuratelier.fleur_atelier_api.application.dto.CommandeResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.LigneCommandeResponse;
import com.fleuratelier.fleur_atelier_api.domain.model.Commande;
import com.fleuratelier.fleur_atelier_api.domain.model.LigneCommande;
import com.fleuratelier.fleur_atelier_api.domain.model.Utilisateur;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommandeMapper {

    private final LivraisonMapper livraisonMapper;

    public CommandeResponse toResponse(Commande commande) {
        List<LigneCommandeResponse> lignes = commande.getLignes() == null
                ? List.of()
                : commande.getLignes().stream().map(this::toLigneResponse).toList();

        Utilisateur u = commande.getUtilisateur();

        return new CommandeResponse(
                commande.getId(),
                commande.getMontantTotal(),
                commande.getStatut(),
                commande.getDateCommande(),
                commande.getDatePaiement(),
                commande.getModePaiement(),
                lignes,
                livraisonMapper.toResponse(commande.getLivraison()),
                u != null ? u.getId() : null,
                u != null ? u.getPrenom() : null,
                u != null ? u.getNom() : null,
                u != null ? u.getEmail() : null
        );
    }

    private LigneCommandeResponse toLigneResponse(LigneCommande ligne) {
        return new LigneCommandeResponse(
                ligne.getId(),
                ligne.getArticleId(),
                ligne.getNomArticle(),
                ligne.getPrixUnitaire(),
                ligne.getQuantite(),
                ligne.getPrixUnitaire().multiply(java.math.BigDecimal.valueOf(ligne.getQuantite()))
        );
    }
}
