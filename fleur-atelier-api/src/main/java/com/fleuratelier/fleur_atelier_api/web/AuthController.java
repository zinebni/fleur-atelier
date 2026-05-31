// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/AuthController.java
package com.fleuratelier.fleur_atelier_api.web;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fleuratelier.fleur_atelier_api.application.dto.AuthResponse;
import com.fleuratelier.fleur_atelier_api.application.dto.LoginRequest;
import com.fleuratelier.fleur_atelier_api.application.dto.RegisterRequest;
import com.fleuratelier.fleur_atelier_api.application.usecase.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Stateless JWT logout.
     * The server has no session to invalidate — this endpoint signals the client
     * to discard its token. The token remains technically valid until expiry,
     * which is acceptable for a stateless architecture.
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of(
                "message", "Logged out successfully. Please discard your token."
        ));
    }
}

