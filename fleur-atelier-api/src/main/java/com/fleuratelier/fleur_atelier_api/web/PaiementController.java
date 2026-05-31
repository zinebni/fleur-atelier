// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/PaiementController.java
package com.fleuratelier.fleur_atelier_api.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fleuratelier.fleur_atelier_api.application.dto.CommandeResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.PaiementRequest;
import com.fleuratelier.fleur_atelier_api.application.usecase.CommandeService;
import com.fleuratelier.fleur_atelier_api.infrastructure.security.UtilisateurPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@Validated
public class PaiementController {

    private final CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeResponse> payer(
            @Valid @RequestBody PaiementRequest request,
            @AuthenticationPrincipal UtilisateurPrincipal principal
    ) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return ResponseEntity.ok(
                commandeService.payerCommande(request, principal.getId())
        );
    }
}
