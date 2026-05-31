// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/AuthResponse.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private final String token;
    private final Long id;
    private final String email;
    private final String role;
    private final String prenom;
    private final String nom;
}
