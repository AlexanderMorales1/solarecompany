/**
 * Entidades JPA de líneas de detalle de pedidos.
 * <p>
 * {@link OrderDetailEntity} guarda cantidad y precio unitario al momento de la compra
 * (histórico), referenciando {@link ProductEntity} y perteneciendo a {@link OrderEntity}.
 */
package com.solare.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Línea de un pedido: producto, cantidad y precio unitario congelado.
 */
@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailEntity {

    /** Identificador de la línea de detalle. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Pedido al que pertenece esta línea. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    /** Producto vendido en esta línea. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    /** Cantidad comprada. */
    @Column(nullable = false)
    private int quantity;

    /** Precio unitario en COP aplicado en el momento del pedido. */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal unitPriceCop;
}
