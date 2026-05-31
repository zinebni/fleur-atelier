// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/HealthController.java
package com.fleuratelier.fleur_atelier_api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API is running");
    }
}
