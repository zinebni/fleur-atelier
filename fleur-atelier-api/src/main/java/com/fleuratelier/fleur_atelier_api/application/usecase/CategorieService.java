// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/usecase/CategorieService.java
package com.fleuratelier.fleur_atelier_api.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fleuratelier.fleur_atelier_api.application.dto.CategorieResponse;
import com.fleuratelier.fleur_atelier_api.application.mapper.CategorieMapper;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.CategorieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategorieService {

    private final CategorieRepository categorieRepository;
    private final CategorieMapper categorieMapper;

    @Transactional(readOnly = true)
    public List<CategorieResponse> getAllCategories() {
        return categorieRepository.findAll().stream()
                .map(categorieMapper::toResponse)
                .toList();
    }
}
