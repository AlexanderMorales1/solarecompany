/**
 * DTOs de pedidos: línea de detalle.
 * <p>
 * Subconjunto de información por producto dentro de un {@link OrderDto}.
 */
package com.solare.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Línea de pedido con precio unitario histórico.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineDto {

    /** Identificador del producto. */
    private Long productId;

    /** Nombre del producto al momento de la compra. */
    private String productName;

    /** Cantidad comprada. */
    private int quantity;

    /** Precio unitario en COP aplicado en el pedido. */
    private BigDecimal unitPriceCop;
}
