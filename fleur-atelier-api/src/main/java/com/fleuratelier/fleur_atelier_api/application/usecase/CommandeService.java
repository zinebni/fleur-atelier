// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/usecase/CommandeService.java
package com.fleuratelier.fleur_atelier_api.application.usecase;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fleuratelier.fleur_atelier_api.application.dto.CommandeResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.PaiementRequest;
import com.fleuratelier.fleur_atelier_api.application.mapper.CommandeMapper;
import com.fleuratelier.fleur_atelier_api.domain.enums.StatutCommande;
import com.fleuratelier.fleur_atelier_api.domain.enums.StatutLivraison;
import com.fleuratelier.fleur_atelier_api.domain.exception.InsufficientStockException;
import com.fleuratelier.fleur_atelier_api.domain.model.Article;
import com.fleuratelier.fleur_atelier_api.domain.model.Bouquet;
import com.fleuratelier.fleur_atelier_api.domain.model.Commande;
import com.fleuratelier.fleur_atelier_api.domain.model.LigneBouquet;
import com.fleuratelier.fleur_atelier_api.domain.model.LigneCommande;
import com.fleuratelier.fleur_atelier_api.domain.model.Livraison;
import com.fleuratelier.fleur_atelier_api.infrastructure.payment.PaymentContext;
import com.fleuratelier.fleur_atelier_api.infrastructure.payment.PaymentResult;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.ArticleRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.BouquetRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.CommandeRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.LivraisonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final BouquetRepository bouquetRepository;
    private final ArticleRepository articleRepository;
    private final LivraisonRepository livraisonRepository;
    private final PaymentContext paymentContext;
    private final CommandeMapper commandeMapper;

    @Transactional
    public CommandeResponse createCommande(Long utilisateurId) {
        Bouquet bouquet = bouquetRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bouquet is empty"));

        if (bouquet.getLignes() == null || bouquet.getLignes().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bouquet is empty");
        }

        Commande commande = Commande.builder()
                .utilisateur(bouquet.getUtilisateur())
                .montantTotal(BigDecimal.ZERO)
                .statut(StatutCommande.EN_ATTENTE)
                .dateCommande(Instant.now())
                .build();

        BigDecimal montantTotal = BigDecimal.ZERO;
        List<LigneCommande> lignesCommande = new ArrayList<>(bouquet.getLignes().size());

        for (LigneBouquet ligneBouquet : bouquet.getLignes()) {
            Article article = ligneBouquet.getArticle();
            BigDecimal ligneTotal = article.getPrix()
                    .multiply(BigDecimal.valueOf(ligneBouquet.getQuantite()));
            montantTotal = montantTotal.add(ligneTotal);

            LigneCommande ligneCommande = LigneCommande.builder()
                    .commande(commande)
                    .articleId(article.getId())
                    .nomArticle(article.getNom())
                    .prixUnitaire(article.getPrix())
                    .quantite(ligneBouquet.getQuantite())
                    .build();
            lignesCommande.add(ligneCommande);
        }

        commande.setMontantTotal(montantTotal);
        commande.setLignes(lignesCommande);

        Commande saved = commandeRepository.save(commande);

        bouquet.getLignes().clear();
        bouquetRepository.save(bouquet);

        log.info("Commande created: commandeId={}, utilisateurId={}, montant={}",
                saved.getId(), utilisateurId, saved.getMontantTotal());
        return commandeMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CommandeResponse> getCommandesByUser(Long utilisateurId) {
        return commandeRepository.findByUtilisateurIdOrderByDateCommandeDesc(utilisateurId).stream()
                .map(commandeMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CommandeResponse getCommandeById(Long commandeId, Long utilisateurId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande not found"));

        Long ownerId = commande.getUtilisateur() != null ? commande.getUtilisateur().getId() : null;
        if (ownerId == null || !ownerId.equals(utilisateurId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return commandeMapper.toResponse(commande);
    }

    @Transactional
    public CommandeResponse payerCommande(PaiementRequest request, Long utilisateurId) {
        Commande commande = commandeRepository.findById(request.getCommandeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande not found"));

        Long ownerId = commande.getUtilisateur() != null ? commande.getUtilisateur().getId() : null;
        if (ownerId == null || !ownerId.equals(utilisateurId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        if (commande.getStatut() != StatutCommande.EN_ATTENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Commande is not payable");
        }

        // ── Stock pre-check ───────────────────────────────────────────────────
        Map<Long, Article> articlesById = new HashMap<>();
        for (LigneCommande ligne : commande.getLignes()) {
            Article article = articleRepository.findById(ligne.getArticleId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Article not found"));
            if (article.getStock() < ligne.getQuantite()) {
                throw new InsufficientStockException("Insufficient stock for article id " + ligne.getArticleId());
            }
            articlesById.put(article.getId(), article);
        }

        // ── Payment ───────────────────────────────────────────────────────────
        PaymentResult result;
        try {
            result = paymentContext.pay(request.getModePaiement(), commande);
        } catch (IllegalArgumentException ex) {
            log.warn("Payment rejected: commandeId={}, mode={}", request.getCommandeId(), request.getModePaiement());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        if (!result.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment failed");
        }

        // ── Decrement stock ───────────────────────────────────────────────────
        for (LigneCommande ligne : commande.getLignes()) {
            Article article = articlesById.get(ligne.getArticleId());
            int newStock = article.getStock() - ligne.getQuantite();
            if (newStock < 0) throw new InsufficientStockException("Insufficient stock for article id " + ligne.getArticleId());
            article.setStock(newStock);
            if (newStock == 0) article.setDisponible(false);
            articleRepository.save(article);
        }

        commande.setStatut(StatutCommande.PAYEE);
        commande.setModePaiement(request.getModePaiement());
        commande.setDatePaiement(Instant.now());
        Commande saved = commandeRepository.save(commande);

        // ── Create Livraison ──────────────────────────────────────────────────
        Livraison livraison = Livraison.builder()
                .commande(saved)
                .adresse(request.getAdresse())
                .ville(request.getVille())
                .codePostal(request.getCodePostal())
                .telephone(request.getTelephone())
                .pays(request.getPays() != null ? request.getPays() : "Maroc")
                .fraisLivraison(new BigDecimal("25.00"))
                .statut(StatutLivraison.EN_PREPARATION)
                .dateCreation(Instant.now())
                .build();
        livraisonRepository.save(livraison);
        saved.setLivraison(livraison);

        log.info("Payment success + livraison created: commandeId={}, transactionId={}",
                saved.getId(), result.getTransactionId());
        return commandeMapper.toResponse(saved);
    }
}
