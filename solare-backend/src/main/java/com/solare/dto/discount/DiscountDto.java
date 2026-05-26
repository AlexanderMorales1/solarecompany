/**
 * DTOs de descuentos: lectura y listados.
 * <p>
 * Refleja campos de {@link com.solare.model.entity.DiscountEntity} para la UI de promociones.
 */
package com.solare.dto.discount;

import com.solare.model.entity.DiscountEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Descuento expuesto en respuestas de la API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {

    /** Identificador del descuento. */
    private Long id;

    /** Código promocional. */
    private String code;

    /** Nombre descriptivo. */
    private String name;

    /** Tipo de mecanismo de descuento. */
    private DiscountEntity.DiscountType type;

    /** Porcentaje cuando aplica. */
    private BigDecimal valuePercent;

    /** Indicador de activación. */
    private boolean active;

    /** Inicio de vigencia. */
    private Instant startsAt;

    /** Fin de vigencia. */
    private Instant endsAt;
}
