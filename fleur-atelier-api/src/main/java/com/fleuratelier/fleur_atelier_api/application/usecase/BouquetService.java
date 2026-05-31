// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/usecase/BouquetService.java
package com.fleuratelier.fleur_atelier_api.application.usecase;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fleuratelier.fleur_atelier_api.application.dto.BouquetResponse;
import com.fleuratelier.fleur_atelier_api.application.mapper.BouquetMapper;
import com.fleuratelier.fleur_atelier_api.domain.exception.InsufficientStockException;
import com.fleuratelier.fleur_atelier_api.domain.model.Article;
import com.fleuratelier.fleur_atelier_api.domain.model.Bouquet;
import com.fleuratelier.fleur_atelier_api.domain.model.LigneBouquet;
import com.fleuratelier.fleur_atelier_api.domain.model.Utilisateur;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.ArticleRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.BouquetRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.LigneBouquetRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.UtilisateurRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.security.UtilisateurPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BouquetService {

    private final BouquetRepository bouquetRepository;
    private final LigneBouquetRepository ligneBouquetRepository;
    private final ArticleRepository articleRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final BouquetMapper bouquetMapper;

    @Transactional
    public BouquetResponse getBouquetByUser() {
        Utilisateur utilisateur = getCurrentUser();
        Bouquet bouquet = getOrCreateBouquet(utilisateur);
        return bouquetMapper.toResponse(bouquet);
    }

    @Transactional
    public BouquetResponse addArticleToBouquet(Long articleId, Integer quantite) {
        validateQuantite(quantite);

        Utilisateur utilisateur = getCurrentUser();
        Bouquet bouquet = getOrCreateBouquet(utilisateur);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        if (!article.isDisponible() || article.getStock() < quantite) {
            throw new InsufficientStockException("Insufficient stock for article id " + articleId);
        }

        LigneBouquet ligne = ligneBouquetRepository.findByBouquetIdAndArticleId(bouquet.getId(), articleId)
                .orElse(null);

        int desiredQuantity = quantite;

        if (ligne == null) {
            ligne = LigneBouquet.builder()
                    .bouquet(bouquet)
                    .article(article)
                    .quantite(desiredQuantity)
                    .build();
        } else {
            desiredQuantity = ligne.getQuantite() + quantite;
            if (article.getStock() < desiredQuantity) {
                throw new InsufficientStockException("Insufficient stock for article id " + articleId);
            }
            ligne.setQuantite(desiredQuantity);
        }

        ligneBouquetRepository.save(ligne);
        return loadBouquetResponse(utilisateur);
    }

    @Transactional
    public BouquetResponse updateQuantite(Long ligneId, Integer quantite) {
        validateQuantite(quantite);

        Utilisateur utilisateur = getCurrentUser();
        LigneBouquet ligne = ligneBouquetRepository.findByIdAndBouquetUtilisateurId(ligneId, utilisateur.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Line not found"));

        if (ligne.getArticle().getStock() < quantite) {
            throw new InsufficientStockException(
                    "Insufficient stock for article id " + ligne.getArticle().getId()
            );
        }

        ligne.setQuantite(quantite);
        ligneBouquetRepository.save(ligne);

        return loadBouquetResponse(utilisateur);
    }

    @Transactional
    public BouquetResponse removeArticle(Long ligneId) {
        Utilisateur utilisateur = getCurrentUser();
        LigneBouquet ligne = ligneBouquetRepository.findByIdAndBouquetUtilisateurId(ligneId, utilisateur.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Line not found"));

        ligneBouquetRepository.delete(ligne);
        return loadBouquetResponse(utilisateur);
    }

    private Utilisateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String email;
        if (principal instanceof UtilisateurPrincipal userPrincipal) {
            email = userPrincipal.getEmail();
        } else if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof String principalString) {
            email = principalString;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Bouquet getOrCreateBouquet(Utilisateur utilisateur) {
        return bouquetRepository.findByUtilisateurId(utilisateur.getId())
                .orElseGet(() -> bouquetRepository.save(Bouquet.builder()
                        .utilisateur(utilisateur)
                        .build()));
    }

    private BouquetResponse loadBouquetResponse(Utilisateur utilisateur) {
        Bouquet bouquet = getOrCreateBouquet(utilisateur);
        return bouquetMapper.toResponse(bouquet);
    }

    private void validateQuantite(Integer quantite) {
        if (quantite == null || quantite < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantite must be >= 1");
        }
    }
}
