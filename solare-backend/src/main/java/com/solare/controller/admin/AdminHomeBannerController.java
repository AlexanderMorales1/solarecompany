/**
 * Administración de banners del home (multipart, rol ADMIN).
 */
package com.solare.controller.admin;

import com.solare.dto.banner.HomeBannerCreateRequest;
import com.solare.dto.banner.HomeBannerDto;
import com.solare.service.HomeBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * CRUD de banners del home con subida multipart ({@code /admin/home-banners}).
 */
@RestController
@RequestMapping("/admin/home-banners")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin — Home Banners")
@SecurityRequirement(name = "bearerAuth")
public class AdminHomeBannerController {

    private final HomeBannerService homeBannerService;

    /** Lista todos los banners (activos e inactivos). */
    @GetMapping
    @Operation(summary = "Listar todos los banners")
    public ResponseEntity<List<HomeBannerDto>> listAll() {
        return ResponseEntity.ok(homeBannerService.listAll());
    }

    /** Crea banner con imagen obligatoria en multipart. */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear banner con imagen")
    public ResponseEntity<HomeBannerDto> create(
            @Valid @ModelAttribute HomeBannerCreateRequest request,
            @ModelAttribute("imageFile") MultipartFile imageFile) {
        return ResponseEntity.ok(homeBannerService.create(request, imageFile));
    }

    /** Actualiza metadatos y opcionalmente reemplaza la imagen. */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Actualizar banner (metadatos e imagen opcional)")
    public ResponseEntity<HomeBannerDto> update(
            @PathVariable Long id,
            @Valid @ModelAttribute HomeBannerCreateRequest request,
            @ModelAttribute("imageFile") MultipartFile imageFile) {
        return ResponseEntity.ok(homeBannerService.update(id, request, imageFile));
    }

    /** Elimina un banner por id. */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar banner")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        homeBannerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
