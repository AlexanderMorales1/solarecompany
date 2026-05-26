/**
 * DTOs de productos: alta y actualización (administración).
 * <p>
 * Entrada validada para crear o modificar {@link com.solare.model.entity.ProductEntity};
 * referencia entidades por códigos y slugs en lugar de IDs anidados.
 */
package com.solare.dto.product;

import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.ProductEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Payload de creación o edición de producto desde el backoffice.
 */
@Data
public class ProductCreateUpdateDto {

    /** Nombre del producto. */
    @NotBlank
    @Size(max = 200)
    private String name;

    /** Marca seleccionada por código. */
    @NotNull
    private BrandEntity.BrandCode brandCode;

    /** Tipo de producto. */
    @NotNull
    private ProductEntity.ProductType productType;

    /** Público por género. */
    @NotNull
    private ProductEntity.GenderTarget gender;

    /** Precio de venta en COP (no negativo). */
    @NotNull
    @Min(0)
    private BigDecimal priceCop;

    /** Descripción opcional. */
    @Size(max = 5000)
    private String description;

    /** Unidades en inventario. */
    @NotNull
    @Min(0)
    private Integer stock;

    /** Marcar como destacado. */
    private boolean featured;

    /** ID de descuento opcional a vincular. */
    private Long discountId;

    /** Lista de URLs de imágenes subidas. */
    private List<@NotBlank @Size(max = 1024) String> imageUrls;

    /** Slugs de categorías a asociar. */
    private Set<@NotBlank String> categorySlugs;
}
