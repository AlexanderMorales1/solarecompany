/**
 * Listado público de categorías del catálogo.
 */
package com.solare.controller;

import com.solare.dto.category.CategoryDto;
import com.solare.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint público de categorías bajo {@code /categories}.
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categorías")
public class CategoryController {

    private final CategoryService categoryService;

    /** Lista todas las categorías disponibles para filtros del catálogo. */
    @GetMapping
    @Operation(summary = "Listar categorías")
    public ResponseEntity<List<CategoryDto>> list() {
        return ResponseEntity.ok(categoryService.listAll());
    }
}
