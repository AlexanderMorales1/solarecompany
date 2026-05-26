/**
 * Entidades JPA de pedidos y checkout.
 * <p>
 * {@link OrderEntity} agrupa el total, estado, método de pago y datos de envío (snapshot).
 * Las líneas detalladas viven en {@link OrderDetailEntity}. Relacionado con
 * {@code OrderRepository} y {@code com.solare.dto.order}.
 */
package com.solare.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido confirmado con totales, estado y datos de contacto/envío congelados al comprar.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    /** Identificador interno del pedido. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario que realizó la compra. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /** Importe total del pedido en COP. */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalCop;

    /** Estado del ciclo de vida del pedido. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderStatus status;

    /** Pasarela o método de pago seleccionado en checkout. */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 32)
    private PaymentMethod paymentMethod;

    /** Nombre completo del comprador capturado en checkout (snapshot). */
    @Column(name = "customer_full_name", length = 200)
    private String customerFullName;

    /** Correo de contacto en el momento de la compra. */
    @Column(name = "customer_email", length = 255)
    private String customerEmail;

    /** Teléfono de contacto en el momento de la compra. */
    @Column(name = "customer_phone", length = 32)
    private String customerPhone;

    /** País de envío. */
    @Column(name = "ship_country", length = 100)
    private String country;

    /** Departamento o estado de envío. */
    @Column(name = "ship_department", length = 100)
    private String department;

    /** Ciudad de envío. */
    @Column(name = "ship_city", length = 100)
    private String city;

    /** Barrio o localidad de entrega. */
    @Column(name = "ship_neighborhood", length = 120)
    private String neighborhood;

    /** Dirección postal detallada. */
    @Column(name = "ship_address", length = 500)
    private String address;

    /** Fecha y hora de creación del pedido. */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /** Líneas de detalle (productos y cantidades) del pedido. */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderDetailEntity> details = new ArrayList<>();

    /**
     * Estados posibles de un pedido en fulfillment y pago.
     */
    public enum OrderStatus {
        /** Creado, pendiente de confirmación de pago. */
        PENDING,
        /** Pago confirmado. */
        PAID,
        /** Enviado al cliente. */
        SHIPPED,
        /** Cancelado. */
        CANCELLED
    }

    /**
     * Métodos de pago integrados o ofrecidos en checkout.
     */
    public enum PaymentMethod {
        /** Financiación Sistecrédito. */
        SISTECREDITO,
        /** Financiación Addi. */
        ADDI,
        /** Pasarela Bold. */
        BOLD,
        /** Pasarela Wompi. */
        WOMPI
    }
}
