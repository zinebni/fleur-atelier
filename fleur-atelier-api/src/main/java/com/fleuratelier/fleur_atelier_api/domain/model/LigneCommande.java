// File: src/main/java/com/fleuratelier/fleur_atelier_api/domain/model/LigneCommande.java
package com.fleuratelier.fleur_atelier_api.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lignes_commande")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @NotNull
    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String nomArticle;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prixUnitaire;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantite;
}
