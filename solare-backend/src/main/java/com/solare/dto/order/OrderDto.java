/**
 * DTOs de pedidos: lectura e historial.
 * <p>
 * Proyección de {@link com.solare.model.entity.OrderEntity} con líneas en {@link OrderLineDto}
 * para el área de cuenta y administración.
 */
package com.solare.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Pedido completo expuesto al cliente o admin.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    /** Identificador del pedido. */
    private Long id;

    /** Total pagado o a pagar en COP. */
    private BigDecimal totalCop;

    /** Estado del pedido como cadena. */
    private String status;

    /** Método de pago seleccionado. */
    private String paymentMethod;

    /** Nombre del comprador (snapshot). */
    private String customerFullName;

    /** Email de contacto. */
    private String customerEmail;

    /** Teléfono de contacto. */
    private String customerPhone;

    /** País de envío. */
    private String country;

    /** Departamento de envío. */
    private String department;

    /** Ciudad de envío. */
    private String city;

    /** Barrio de envío. */
    private String neighborhood;

    /** Dirección de entrega. */
    private String address;

    /** Fecha de creación del pedido. */
    private Instant createdAt;

    /** Líneas de productos del pedido. */
    private List<OrderLineDto> lines;
}
