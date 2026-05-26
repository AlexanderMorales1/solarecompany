/**
 * DTOs de medios: respuesta tras subir imágenes.
 * <p>
 * Devuelto por el endpoint de carga de archivos usado en productos y banners.
 */
package com.solare.dto.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metadatos del archivo almacenado tras una subida exitosa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {

    /** URL pública o relativa para acceder a la imagen. */
    private String url;

    /** Nombre del archivo en disco. */
    private String fileName;

    /** Tipo MIME detectado. */
    private String contentType;

    /** Tamaño en bytes. */
    private long size;
}
