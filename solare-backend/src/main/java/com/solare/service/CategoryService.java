/**
 * Servicio de lectura del catálogo de categorías.
 * <p>
 * Relación: {@link com.solare.controller.CategoryController}.
 * </p>
 */
package com.solare.service;

import com.solare.dto.category.CategoryDto;
import com.solare.model.entity.CategoryEntity;
import com.solare.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Listado simple de categorías para filtros y navegación en tienda. */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Devuelve todas las categorías como DTO.
     *
     * @return lista de categorías
     */
    @Transactional(readOnly = true)
    public List<CategoryDto> listAll() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    /** Mapea entidad de categoría a DTO de respuesta. */
    private CategoryDto toDto(CategoryEntity entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .build();
    }
}
