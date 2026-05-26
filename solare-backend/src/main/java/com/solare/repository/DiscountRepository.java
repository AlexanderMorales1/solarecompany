/**
 * Repositorios Spring Data JPA para descuentos y promociones.
 * <p>
 * Soporta listados de cupones activos para administración y aplicación en catálogo.
 */
package com.solare.repository;

import com.solare.model.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Acceso a datos de {@link DiscountEntity}.
 */
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {

    /**
     * Lista todos los descuentos marcados como activos.
     *
     * @return colección de descuentos con {@link DiscountEntity#active} en {@code true}
     */
    List<DiscountEntity> findByActiveTrue();
}
