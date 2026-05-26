/**
 * Entidades JPA del carrito de compras por usuario.
 * <p>
 * Cada fila de {@link CartEntity} representa una línea (usuario + producto + cantidad).
 * La restricción única en {@code user_id} y {@code product_id} evita duplicados. Se gestiona
 * desde {@code CartRepository} y los DTOs en {@code com.solare.dto.cart}.
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Línea del carrito: cantidad de un {@link ProductEntity} para un {@link UserEntity}.
 */
@Entity
@Table(
        name = "cart",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {

    /** Identificador de la línea del carrito. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Propietario del carrito. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /** Producto añadido al carrito. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    /** Cantidad de unidades solicitadas. */
    @Column(nullable = false)
    private int quantity;
}
