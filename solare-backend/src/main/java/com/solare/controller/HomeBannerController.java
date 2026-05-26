/**
 * Endpoint público de banners activos para la página de inicio.
 */
package com.solare.controller;

import com.solare.dto.banner.HomeBannerDto;
import com.solare.service.HomeBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Banners activos de la página principal ({@code /home-banners}).
 */
@RestController
@RequestMapping("/home-banners")
@RequiredArgsConstructor
@Tag(name = "Home Banners")
public class HomeBannerController {

    private final HomeBannerService homeBannerService;

    /** Lista banners activos ordenados para el carrusel del home. */
    @GetMapping
    @Operation(summary = "Listar banners activos para la pantalla principal")
    public ResponseEntity<List<HomeBannerDto>> listPublic() {
        return ResponseEntity.ok(homeBannerService.listPublic());
    }
}
