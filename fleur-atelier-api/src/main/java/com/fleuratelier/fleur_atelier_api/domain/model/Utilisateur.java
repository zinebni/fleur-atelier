// File: src/main/java/com/fleuratelier/fleur_atelier_api/domain/model/Utilisateur.java
package com.fleuratelier.fleur_atelier_api.domain.model;

import com.fleuratelier.fleur_atelier_api.domain.enums.RoleUtilisateur;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleUtilisateur role;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String prenom;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nom;
}
