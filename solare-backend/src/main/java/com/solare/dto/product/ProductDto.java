/**
 * DTOs de productos: lectura del catálogo.
 * <p>
 * Agrega datos de marca, descuento y categorías en formato apto para el frontend,
 * mapeado desde {@link com.solare.model.entity.ProductEntity} en la capa de servicio.
 */
package com.solare.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Vista de producto para listados, detalle y carrito.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    /** Identificador del producto. */
    private Long id;

    /** Nombre comercial. */
    private String name;

    /** Código de marca ({@link com.solare.model.entity.BrandEntity.BrandCode}). */
    private String brandCode;

    /** Nombre legible de la marca. */
    private String brandDisplayName;

    /** Tipo de producto como cadena. */
    private String productType;

    /** Género objetivo como cadena. */
    private String gender;

    /** Precio en COP. */
    private BigDecimal priceCop;

    /** Descripción del producto. */
    private String description;

    /** Stock disponible. */
    private int stock;

    /** Si está destacado. */
    private boolean featured;

    /** ID del descuento asociado, si existe. */
    private Long discountId;

    /** Código del cupón/descuento. */
    private String discountCode;

    /** Tipo de descuento aplicable. */
    private String discountType;

    /** URLs de imágenes. */
    private List<String> imageUrls;

    /** Slugs de categorías vinculadas. */
    private Set<String> categorySlugs;

    /** Fecha de alta del producto. */
    private Instant createdAt;
}
