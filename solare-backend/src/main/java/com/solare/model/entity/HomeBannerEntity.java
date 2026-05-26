/**
 * Entidades JPA de banners de la página de inicio.
 * <p>
 * {@link HomeBannerEntity} alimenta el carrusel o hero del frontend. Se ordena por
 * {@link #displayOrder} y se filtra por {@link #active} en {@code HomeBannerRepository}.
 */
package com.solare.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Banner promocional mostrado en la home de la tienda.
 */
@Entity
@Table(name = "home_banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeBannerEntity {

    /** Identificador interno del banner. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** URL de la imagen del banner. */
    @Column(name = "image_url", nullable = false, length = 1024)
    private String imageUrl;

    /** Título opcional superpuesto o asociado al banner. */
    @Column(length = 255)
    private String title;

    /** Subtítulo o texto secundario. */
    @Column(length = 500)
    private String subtitle;

    /** Si el banner debe mostrarse en la home. */
    @Column(nullable = false)
    private boolean active;

    /** Orden relativo de aparición (menor valor = más prioritario). */
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    /** Fecha de creación del registro. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Callback JPA antes de insertar: inicializa {@link #createdAt} si no está definido.
     */
    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
