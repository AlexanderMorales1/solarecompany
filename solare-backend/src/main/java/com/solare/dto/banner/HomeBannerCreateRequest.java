/**
 * DTOs de banners: creación con metadatos opcionales.
 * <p>
 * Complementa el archivo de imagen subido por multipart; la URL se asigna en servicio.
 */
package com.solare.dto.banner;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Metadatos enviados al crear un banner (título, estado, orden).
 */
@Data
public class HomeBannerCreateRequest {

    /** Título del banner. */
    @Size(max = 255)
    private String title;

    /** Subtítulo del banner. */
    @Size(max = 500)
    private String subtitle;

    /** Estado activo; {@code null} puede interpretarse como valor por defecto en servicio. */
    private Boolean active;

    /** Posición en el carrusel (máximo 9999). */
    @Max(9999)
    private Integer displayOrder;
}
