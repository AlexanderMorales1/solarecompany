/**
 * Entidades JPA de categorías del catálogo.
 * <p>
 * {@link CategoryEntity} participa en la relación muchos-a-muchos con {@link ProductEntity}
 * ({@code product_categories}). El {@code slug} se usa en filtros y en DTOs
 * ({@code com.solare.dto.category}).
 */
package com.solare.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Categoría de clasificación de productos (navegación y filtros).
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {

    /** Identificador interno de la categoría. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre visible de la categoría. */
    @Column(nullable = false, length = 100)
    private String name;

    /** Identificador URL amigable y único para rutas y consultas. */
    @Column(nullable = false, unique = true, length = 64)
    private String slug;

    /**
     * Productos vinculados; lado inverso de la asociación en {@link ProductEntity#categories}.
     */
    @ManyToMany(mappedBy = "categories")
    @Builder.Default
    private Set<ProductEntity> products = new HashSet<>();
}
