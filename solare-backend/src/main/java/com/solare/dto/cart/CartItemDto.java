/**
 * DTOs del carrito de compras.
 * <p>
 * Representa una línea del carrito con el producto embebido como {@link com.solare.dto.product.ProductDto}.
 */
package com.solare.dto.cart;

import com.solare.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ítem individual del carrito (cantidad + detalle de producto).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    /** Identificador de la fila en tabla {@code cart}. */
    private Long cartItemId;

    /** Unidades en el carrito. */
    private int quantity;

    /** Producto asociado a la línea. */
    private ProductDto product;
}
