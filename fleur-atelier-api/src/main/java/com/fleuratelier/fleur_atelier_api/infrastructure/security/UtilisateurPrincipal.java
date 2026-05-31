// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/security/UtilisateurPrincipal.java
package com.fleuratelier.fleur_atelier_api.infrastructure.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fleuratelier.fleur_atelier_api.domain.enums.RoleUtilisateur;
import com.fleuratelier.fleur_atelier_api.domain.model.Utilisateur;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UtilisateurPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final RoleUtilisateur role;
    private final String prenom;
    private final String nom;

    public static UtilisateurPrincipal from(Utilisateur utilisateur) {
        return new UtilisateurPrincipal(
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getPassword(),
                utilisateur.getRole(),
                utilisateur.getPrenom(),
                utilisateur.getNom()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
