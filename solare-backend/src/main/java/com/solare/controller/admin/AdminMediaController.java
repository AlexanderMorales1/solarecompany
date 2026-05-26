/**
 * Subida de imágenes al almacenamiento local (carpeta configurable).
 */
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

/**
 * Subida de imágenes al almacenamiento local ({@code /admin/media}).
 */
@RestController
@RequestMapping("/admin/media")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin — Archivos")
@SecurityRequirement(name = "bearerAuth")
public class AdminMediaController {

    private final ImageStorageService imageStorageService;

    /**
     * Sube uno o varios archivos de imagen a la carpeta indicada.
     *
     * @param files  archivos multipart
     * @param folder subcarpeta destino (por defecto {@code products})
     * @return metadatos y URLs públicas de cada archivo guardado
     */
    @PostMapping("/images")
    @Operation(summary = "Subir una o múltiples imágenes")
    public ResponseEntity<List<ImageUploadResponse>> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(defaultValue = "products") String folder) {
        return ResponseEntity.ok(imageStorageService.saveImages(files, folder));
    }
}
