/**
 * Repositorios Spring Data JPA para categorías del catálogo.
 * <p>
 * El {@code slug} es la clave usada en URLs del frontend y en filtros de productos.
 */
package com.solare.repository;

import com.solare.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a datos de {@link CategoryEntity}.
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Busca una categoría por su identificador URL.
     *
     * @param slug slug único de la categoría
     * @return categoría encontrada o vacío
     */
    Optional<CategoryEntity> findBySlug(String slug);
}
