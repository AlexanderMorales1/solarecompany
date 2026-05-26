/**
 * Entidades JPA de promociones y descuentos.
 * <p>
 * {@link DiscountEntity} puede asociarse opcionalmente a {@link ProductEntity}. Los servicios
 * de catálogo y carrito evalúan vigencia ({@code startsAt}/{@code endsAt}) y tipo de descuento
 * al calcular precios. Los DTOs viven en {@code com.solare.dto.discount}.
 */
package com.solare.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Regla de descuento aplicable a productos o al carrito según la lógica de negocio.
 */
@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountEntity {

    /** Identificador interno del descuento. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código promocional único (cupón). */
    @Column(nullable = false, unique = true, length = 64)
    private String code;

    /** Descripción corta para administración y UI. */
    @Column(nullable = false, length = 200)
    private String name;

    /** Mecanismo de cálculo del beneficio. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private DiscountType type;

    /** Porcentaje de rebaja cuando {@link #type} es {@link DiscountType#PERCENTAGE}. */
    @Column(precision = 10, scale = 2)
    private BigDecimal valuePercent;

    /** Indica si el descuento está habilitado para su uso. */
    @Column(nullable = false)
    private boolean active;

    /** Inicio de vigencia; {@code null} si no hay límite inferior. */
    @Column(name = "starts_at")
    private Instant startsAt;

    /** Fin de vigencia; {@code null} si no hay límite superior. */
    @Column(name = "ends_at")
    private Instant endsAt;

    /**
     * Tipos de descuento soportados por el dominio.
     */
    public enum DiscountType {
        /** Rebaja porcentual sobre el precio. */
        PERCENTAGE,
        /** Promoción 2x1 (paga uno, lleva dos unidades elegibles). */
        BOGO_TWO_FOR_ONE
    }
}
