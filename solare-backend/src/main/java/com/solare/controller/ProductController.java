/**
 * API pública de consulta de productos (listado, detalle, destacados y recientes).
 */
package com.solare.controller;

import com.solare.dto.product.ProductDto;
import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.ProductEntity;
import com.solare.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Consulta pública del catálogo de productos ({@code /products}).
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Productos")
public class ProductController {

    private final ProductService productService;

    /** Listado paginado con filtros opcionales de marca, género, tipo, categoría y texto. */
    @GetMapping
    @Operation(summary = "Listar y filtrar productos")
    public ResponseEntity<Page<ProductDto>> list(
            @RequestParam(required = false) BrandEntity.BrandCode brand,
            @RequestParam(required = false) ProductEntity.GenderTarget gender,
            @RequestParam(required = false) ProductEntity.ProductType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(required = false) Boolean featured,
            @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(productService.search(brand, gender, type, category, query, featured, pageable));
    }

    /** Detalle de un producto por identificador. */
    @GetMapping("/{id}")
    @Operation(summary = "Detalle de producto")
    public ResponseEntity<ProductDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    /** Productos marcados como destacados para una marca. */
    @GetMapping("/featured/{brand}")
    @Operation(summary = "Destacados por marca")
    public ResponseEntity<List<ProductDto>> featuredByBrand(@PathVariable BrandEntity.BrandCode brand) {
        return ResponseEntity.ok(productService.featuredByBrand(brand));
    }

    /** Últimos productos ordenados por id descendente (sin filtros adicionales). */
    @GetMapping("/recent")
    @Operation(summary = "Productos más recientes")
    public ResponseEntity<Page<ProductDto>> recent(
            @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(productService.search(null, null, null, null, null, null, pageable));
    }
}
