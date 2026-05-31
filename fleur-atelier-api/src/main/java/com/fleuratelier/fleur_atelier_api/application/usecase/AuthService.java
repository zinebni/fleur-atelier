// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/usecase/AuthService.java
package com.fleuratelier.fleur_atelier_api.application.usecase;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fleuratelier.fleur_atelier_api.application.dto.AuthResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.LoginRequest;
import com.fleuratelier.fleur_atelier_api.application.dto.RegisterRequest;
import com.fleuratelier.fleur_atelier_api.domain.enums.RoleUtilisateur;
import com.fleuratelier.fleur_atelier_api.domain.model.Utilisateur;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.UtilisateurRepository;
import com.fleuratelier.fleur_atelier_api.infrastructure.security.JwtService;
import com.fleuratelier.fleur_atelier_api.infrastructure.security.UtilisateurPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleUtilisateur.USER)
                .prenom(request.getPrenom())
                .nom(request.getNom())
                .build();

        Utilisateur saved = utilisateurRepository.save(utilisateur);
        String token = jwtService.generateToken(UtilisateurPrincipal.from(saved));
        return buildAuthResponse(token, saved);
    }

    public AuthResponse login(LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), utilisateur.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(UtilisateurPrincipal.from(utilisateur));
        return buildAuthResponse(token, utilisateur);
    }

    private AuthResponse buildAuthResponse(String token, Utilisateur utilisateur) {
        return new AuthResponse(
                token,
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getRole().name(),
                utilisateur.getPrenom(),
                utilisateur.getNom()
        );
    }
}
