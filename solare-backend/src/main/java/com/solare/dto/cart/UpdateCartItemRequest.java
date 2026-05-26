/**
 * DTOs del carrito: actualizar cantidad de una línea.
 * <p>
 * Usado en PATCH/PUT de una línea existente identificada por {@code cartItemId} en la ruta.
 */
package com.solare.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Nueva cantidad para una línea del carrito.
 */
@Data
public class UpdateCartItemRequest {

    /** Cantidad deseada (mínimo 1). */
    @NotNull
    @Min(1)
    private Integer quantity;
}
