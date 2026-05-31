// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/CommandeController.java
package com.fleuratelier.fleur_atelier_api.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fleuratelier.fleur_atelier_api.application.dto.CommandeResponse;
import com.fleuratelier.fleur_atelier_api.application.usecase.CommandeService;
import com.fleuratelier.fleur_atelier_api.infrastructure.security.UtilisateurPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeResponse> createCommande(
            @AuthenticationPrincipal UtilisateurPrincipal principal
    ) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        CommandeResponse response = commandeService.createCommande(principal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommandeResponse>> getMyCommandes(
            @AuthenticationPrincipal UtilisateurPrincipal principal
    ) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return ResponseEntity.ok(commandeService.getCommandesByUser(principal.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponse> getMyCommandeById(
            @PathVariable("id") Long commandeId,
            @AuthenticationPrincipal UtilisateurPrincipal principal
    ) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return ResponseEntity.ok(commandeService.getCommandeById(commandeId, principal.getId()));
    }
}
