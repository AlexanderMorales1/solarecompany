/**
 * DTOs de categorías del catálogo.
 * <p>
 * Proyección ligera de {@link com.solare.model.entity.CategoryEntity} para listados
 * y filtros en la API pública.
 */
package com.solare.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Categoría expuesta al cliente (identificador, nombre y slug).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    /** Identificador de la categoría. */
    private Long id;

    /** Nombre visible. */
    private String name;

    /** Slug para URLs y filtros. */
    private String slug;
}
