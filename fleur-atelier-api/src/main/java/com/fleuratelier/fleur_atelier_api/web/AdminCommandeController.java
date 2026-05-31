// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/AdminCommandeController.java
package com.fleuratelier.fleur_atelier_api.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fleuratelier.fleur_atelier_api.application.dto.CommandeResponse;
import com.fleuratelier.fleur_atelier_api.application.mapper.CommandeMapper;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.CommandeRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/commandes")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminCommandeController {

    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<CommandeResponse>> getAllCommandes() {
        List<CommandeResponse> list = commandeRepository
                .findAllWithDetails()
                .stream()
                .map(commandeMapper::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<CommandeResponse> getCommandeById(@PathVariable Long id) {
        return commandeRepository.findById(id)
                .map(commandeMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande not found"));
    }
}
