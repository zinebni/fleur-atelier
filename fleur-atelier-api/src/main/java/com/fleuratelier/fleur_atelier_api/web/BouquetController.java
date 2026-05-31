// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/BouquetController.java
package com.fleuratelier.fleur_atelier_api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fleuratelier.fleur_atelier_api.application.dto.AjouterArticleRequest;
import com.fleuratelier.fleur_atelier_api.application.dto.BouquetResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.UpdateQuantiteRequest;
import com.fleuratelier.fleur_atelier_api.application.usecase.BouquetService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bouquet")
@RequiredArgsConstructor
@Validated
public class BouquetController {

    private final BouquetService bouquetService;

    @GetMapping
    public ResponseEntity<BouquetResponse> getBouquet() {
        return ResponseEntity.ok(bouquetService.getBouquetByUser());
    }

    @PostMapping("/ajouter")
    public ResponseEntity<BouquetResponse> addArticle(@Valid @RequestBody AjouterArticleRequest request) {
        return ResponseEntity.ok(bouquetService.addArticleToBouquet(request.getArticleId(), request.getQuantite()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BouquetResponse> updateQuantite(
            @PathVariable("id") @Positive Long ligneId,
            @Valid @RequestBody UpdateQuantiteRequest request
    ) {
        return ResponseEntity.ok(bouquetService.updateQuantite(ligneId, request.getQuantite()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BouquetResponse> removeArticle(@PathVariable("id") @Positive Long ligneId) {
        return ResponseEntity.ok(bouquetService.removeArticle(ligneId));
    }
}
