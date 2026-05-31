package com.fleuratelier.fleur_atelier_api.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

import com.fleuratelier.fleur_atelier_api.domain.enums.StatutLivraison;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "livraisons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commande_id", nullable = false, unique = true)
    private Commande commande;

    @NotBlank
    @Column(nullable = false)
    private String adresse;

    @NotBlank
    @Column(nullable = false)
    private String ville;

    @NotBlank
    @Column(nullable = false)
    private String codePostal;

    @NotBlank
    @Column(nullable = false)
    private String telephone;

    @Builder.Default
    @Column(nullable = false)
    private String pays = "Maroc";

    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fraisLivraison = new BigDecimal("25.00");

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private StatutLivraison statut = StatutLivraison.EN_PREPARATION;

    @Builder.Default
    @Column(nullable = false)
    private Instant dateCreation = Instant.now();

    private Instant dateLivraison;
}
