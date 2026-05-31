// File: src/main/java/com/fleuratelier/fleur_atelier_api/web/FileUploadController.java
package com.fleuratelier.fleur_atelier_api.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/upload")
@PreAuthorize("hasRole('ADMIN')")
public class FileUploadController {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final java.util.Set<String> ALLOWED_TYPES =
            java.util.Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file uploaded");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File exceeds 5 MB limit");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "Only JPEG, PNG, WebP and GIF images are allowed");
        }

        // Determine extension
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        // Unique filename
        String filename = UUID.randomUUID().toString() + extension;

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);
            Path destination = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to store file: " + e.getMessage());
        }

        // Build public URL
        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort();
        String imageUrl = baseUrl + "/uploads/" + filename;

        return ResponseEntity.ok(Map.of("url", imageUrl));
    }
}
