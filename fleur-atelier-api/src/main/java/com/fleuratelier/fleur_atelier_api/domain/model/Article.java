// File: src/main/java/com/fleuratelier/fleur_atelier_api/domain/model/Article.java
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
import jakarta.persistence.Version;
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
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String nom;

    @Size(max = 2000)
    @Column(length = 2000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prix;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private boolean disponible;

    @Size(max = 1000)
    @Column(length = 1000)
    private String imageUrl;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;
}
