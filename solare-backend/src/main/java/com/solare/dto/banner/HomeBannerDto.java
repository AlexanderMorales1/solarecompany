/**
 * DTOs de banners de la página de inicio.
 * <p>
 * Salida de lectura mapeada desde {@link com.solare.model.entity.HomeBannerEntity}.
 */
package com.solare.dto.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Banner para mostrar en el carrusel o hero de la home.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeBannerDto {

    /** Identificador del banner. */
    private Long id;

    /** URL de la imagen. */
    private String imageUrl;

    /** Título opcional. */
    private String title;

    /** Subtítulo opcional. */
    private String subtitle;

    /** Si está activo y debe mostrarse. */
    private boolean active;

    /** Orden de visualización. */
    private int displayOrder;
}
