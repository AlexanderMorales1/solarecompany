/**
 * Entidades JPA de marcas de productos (catálogo).
 * <p>
 * {@link BrandEntity} se referencia desde {@link ProductEntity} y se consulta mediante
 * {@code BrandRepository}. Los códigos de {@link BrandEntity.BrandCode} se exponen en DTOs
 * de producto para el frontend.
 */
package com.solare.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Marca comercial asociada a los productos del catálogo.
 */
@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandEntity {

    /** Identificador interno de la marca. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código estable de la marca (clave de negocio). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 32)
    private BrandCode code;

    /** Nombre legible mostrado en la interfaz. */
    @Column(nullable = false, length = 64)
    private String displayName;

    /**
     * Marcas disponibles en el inventario de Solare.
     */
    public enum BrandCode {
        /** Marca Ferrati. */
        FERRATI,
        /** Marca Ray-Ban. */
        RAYBAN
    }
}
