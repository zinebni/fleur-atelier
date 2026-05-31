// File: src/main/java/com/fleuratelier/fleur_atelier_api/infrastructure/security/UserDetailsServiceImpl.java
package com.fleuratelier.fleur_atelier_api.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fleuratelier.fleur_atelier_api.domain.model.Utilisateur;
import com.fleuratelier.fleur_atelier_api.infrastructure.persistence.UtilisateurRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UtilisateurPrincipal.from(utilisateur);
    }
}
