/**
 * DTOs del carrito: añadir producto.
 * <p>
 * Entrada del endpoint POST de carrito; valida cantidad mínima 1.
 */
package com.solare.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Petición para agregar o incrementar un producto en el carrito.
 */
@Data
public class AddToCartRequest {

    /** ID del producto a añadir. */
    @NotNull
    private Long productId;

    /** Cantidad a sumar (al menos 1). */
    @NotNull
    @Min(1)
    private Integer quantity;
}
