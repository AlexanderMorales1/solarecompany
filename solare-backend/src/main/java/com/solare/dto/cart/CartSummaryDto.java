/**
 * DTOs del carrito: resumen con totales.
 * <p>
 * Respuesta agregada del endpoint de carrito antes del checkout.
 */
package com.solare.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Vista completa del carrito con subtotal y notas informativas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryDto {

    /** Líneas del carrito. */
    private List<CartItemDto> items;

    /** Suma de líneas en COP antes de envío o impuestos adicionales. */
    private BigDecimal subtotalCop;

    /** Mensaje opcional (promociones, avisos de stock, etc.). */
    private String note;
}
