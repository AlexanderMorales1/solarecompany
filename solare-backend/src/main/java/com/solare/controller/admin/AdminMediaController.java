package com.solare.controller.admin;

import com.solare.dto.media.ImageUploadResponse;
import com.solare.service.ImageStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/media")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin — Archivos")
@SecurityRequirement(name = "bearerAuth")
public class AdminMediaController {

    private final ImageStorageService imageStorageService;

    @PostMapping("/images")
    @Operation(summary = "Subir una o múltiples imágenes")
    public ResponseEntity<List<ImageUploadResponse>> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(defaultValue = "products") String folder) {
        return ResponseEntity.ok(imageStorageService.saveImages(files, folder));
    }
}
