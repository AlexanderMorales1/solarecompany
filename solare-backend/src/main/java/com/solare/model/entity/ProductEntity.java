/**
 * Entidades JPA del catálogo de productos.
 * <p>
 * {@link ProductEntity} es el núcleo del inventario: enlaza {@link BrandEntity},
 * {@link DiscountEntity}, {@link CategoryEntity} e imágenes en colección. Es persistida por
 * {@code ProductRepository} y mapeada a {@code com.solare.dto.product}.
 */
package com.solare.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.PrePersist;

/**
 * Producto vendible en la tienda (gafas u otros artículos del catálogo).
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    /** Identificador interno del producto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre comercial del producto. */
    @Column(nullable = false, length = 200)
    private String name;

    /** Marca obligatoria del artículo. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    /** Estilo o línea del producto (casual, deportivo, etc.). */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 32)
    private ProductType productType;

    /** Segmento de género al que está dirigido el producto. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private GenderTarget gender;

    /** Precio de venta en pesos colombianos (COP). */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal priceCop;

    /** Descripción larga en texto libre. */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Unidades disponibles en inventario. */
    @Column(nullable = false)
    private int stock;

    /** Si el producto debe destacarse en la página de inicio o listados. */
    @Column(nullable = false)
    private boolean featured;

    /** Descuento opcional vinculado al producto. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_id")
    private DiscountEntity discount;

    /** URLs de imágenes almacenadas en el sistema de archivos o CDN. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url", length = 1024)
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    /** Categorías asignadas al producto. */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    private Set<CategoryEntity> categories = new HashSet<>();

    /** Marca de tiempo de alta; no se actualiza tras la inserción. */
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Callback JPA ejecutado antes de insertar: asigna {@link #createdAt} si aún es {@code null}.
     * No tiene efectos fuera de la persistencia del registro.
     */
    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    /**
     * Tipo de producto según uso o estilo.
     */
    public enum ProductType {
        /** Uso cotidiano. */
        CASUAL,
        /** Uso deportivo. */
        DEPORTIVO
    }

    /**
     * Público objetivo por género.
     */
    public enum GenderTarget {
        /** Productos para hombre. */
        HOMBRE,
        /** Productos para mujer. */
        MUJER
    }
}
