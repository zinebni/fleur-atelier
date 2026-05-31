// File: src/main/java/com/fleuratelier/fleur_atelier_api/domain/model/Bouquet.java
package com.fleuratelier.fleur_atelier_api.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bouquets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bouquet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @Builder.Default
    @OneToMany(mappedBy = "bouquet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneBouquet> lignes = new ArrayList<>();

    /**
     * Computed — never persisted. Always reflects the live state of lignes.
     */
    public BigDecimal getPrixTotal() {
        if (lignes == null || lignes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return lignes.stream()
                .map(l -> l.getArticle().getPrix()
                        .multiply(BigDecimal.valueOf(l.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
