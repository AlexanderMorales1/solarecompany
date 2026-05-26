/**
 * Repositorios Spring Data JPA para marcas de producto.
 * <p>
 * Resuelve {@link com.solare.model.entity.BrandEntity} por código al crear o actualizar
 * productos desde la API de administración.
 */
package com.solare.repository;

import com.solare.model.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a datos de {@link BrandEntity}.
 */
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {

    /**
     * Busca una marca por su código de negocio.
     *
     * @param code valor de {@link BrandEntity.BrandCode}
     * @return marca encontrada o vacío
     */
    Optional<BrandEntity> findByCode(BrandEntity.BrandCode code);
}
