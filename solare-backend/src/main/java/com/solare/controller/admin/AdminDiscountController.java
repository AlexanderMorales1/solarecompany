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

@RestController
@RequestMapping("/admin/discounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin — Promociones")
@SecurityRequirement(name = "bearerAuth")
public class AdminDiscountController {

    private final DiscountService discountService;

    @GetMapping
    @Operation(summary = "Listar todas las promociones")
    public ResponseEntity<List<DiscountDto>> list() {
        return ResponseEntity.ok(discountService.listAll());
    }

    @PostMapping
    @Operation(summary = "Crear promoción")
    public ResponseEntity<DiscountDto> create(@Valid @RequestBody DiscountUpsertDto dto) {
        return ResponseEntity.ok(discountService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar promoción")
    public ResponseEntity<DiscountDto> update(@PathVariable Long id, @Valid @RequestBody DiscountUpsertDto dto) {
        return ResponseEntity.ok(discountService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar promoción")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        discountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
