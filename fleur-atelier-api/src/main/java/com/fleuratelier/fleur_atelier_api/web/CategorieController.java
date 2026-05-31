// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/CategorieController.java
package com.fleuratelier.fleur_atelier_api.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fleuratelier.fleur_atelier_api.application.dto.CategorieResponse;
import com.fleuratelier.fleur_atelier_api.application.usecase.CategorieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    @GetMapping
    public ResponseEntity<List<CategorieResponse>> getAllCategories() {
        return ResponseEntity.ok(categorieService.getAllCategories());
    }
}
