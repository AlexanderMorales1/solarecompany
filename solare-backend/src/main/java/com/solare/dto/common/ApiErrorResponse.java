/**
 * DTOs transversales: respuestas de error de la API.
 * <p>
 * Formato uniforme devuelto por el manejador global de excepciones y validaciones
 * ({@code @ControllerAdvice}) hacia el frontend.
 */
package com.solare.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Cuerpo JSON estándar para errores HTTP (4xx/5xx).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    /** Marca de tiempo del error. */
    private Instant timestamp;

    /** Código de estado HTTP. */
    private int status;

    /** Frase de estado HTTP (por ejemplo {@code Bad Request}). */
    private String error;

    /** Mensaje legible para el usuario o logs. */
    private String message;

    /** Ruta de la petición que falló. */
    private String path;

    /** Errores por campo en validaciones de formulario ({@code field -> mensaje}). */
    private Map<String, String> fieldErrors;
}
