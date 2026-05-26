/**
 * DTOs de descuentos: creación y actualización.
 * <p>
 * Payload de administración mapeado a {@link com.solare.model.entity.DiscountEntity}.
 */
package com.solare.dto.discount;

import com.solare.model.entity.DiscountEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Datos para insertar o modificar un descuento.
 */
@Data
public class DiscountUpsertDto {

    /** Código único del cupón. */
    @NotBlank
    @Size(max = 64)
    private String code;

    /** Nombre visible del descuento. */
    @NotBlank
    @Size(max = 200)
    private String name;

    /** Tipo de descuento. */
    @NotNull
    private DiscountEntity.DiscountType type;

    /** Valor porcentual para descuentos de tipo porcentaje. */
    private BigDecimal valuePercent;

    /** Si el descuento está habilitado. */
    private boolean active;

    /** Fecha de inicio de vigencia opcional. */
    private Instant startsAt;

    /** Fecha de fin de vigencia opcional. */
    private Instant endsAt;
}
