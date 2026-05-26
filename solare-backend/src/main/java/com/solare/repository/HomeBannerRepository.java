/**
 * Repositorios Spring Data JPA para banners de la página de inicio.
 * <p>
 * Ordena banners activos para el endpoint público de la home.
 */
package com.solare.repository;

import com.solare.model.entity.HomeBannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a datos de {@link HomeBannerEntity}.
 */
public interface HomeBannerRepository extends JpaRepository<HomeBannerEntity, Long> {

    /**
     * Obtiene banners visibles ordenados por prioridad de visualización y fecha de creación.
     *
     * @return lista de banners activos, de menor a mayor {@link HomeBannerEntity#displayOrder}
     */
    List<HomeBannerEntity> findByActiveTrueOrderByDisplayOrderAscCreatedAtAsc();
}
