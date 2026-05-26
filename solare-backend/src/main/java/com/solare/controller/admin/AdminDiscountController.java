/**
 * CRUD de promociones y descuentos (panel admin).
 */
package com.solare.controller.admin;

import com.solare.dto.discount.DiscountDto;
import com.solare.dto.discount.DiscountUpsertDto;
import com.solare.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Mantenimiento de promociones ({@code /admin/discounts}, rol ADMIN).
 */
@RestController
@RequestMapping("/admin/discounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin — Promociones")
@SecurityRequirement(name = "bearerAuth")
public class AdminDiscountController {

    private final DiscountService discountService;

    /** Lista todas las promociones (activas e inactivas). */
    @GetMapping
    @Operation(summary = "Listar todas las promociones")
    public ResponseEntity<List<DiscountDto>> list() {
        return ResponseEntity.ok(discountService.listAll());
    }

    /** Crea una nueva promoción. */
    @PostMapping
    @Operation(summary = "Crear promoción")
    public ResponseEntity<DiscountDto> create(@Valid @RequestBody DiscountUpsertDto dto) {
        return ResponseEntity.ok(discountService.create(dto));
    }

    /** Actualiza una promoción existente. */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar promoción")
    public ResponseEntity<DiscountDto> update(@PathVariable Long id, @Valid @RequestBody DiscountUpsertDto dto) {
        return ResponseEntity.ok(discountService.update(id, dto));
    }

    /** Elimina una promoción por id. */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar promoción")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        discountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
