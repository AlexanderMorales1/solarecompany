/**
 * Excepciones de dominio para solicitudes inválidas o reglas de negocio incumplidas.
 * <p>
 * Relación: lanzada desde servicios y controladores; capturada por
 * {@link com.solare.exception.GlobalExceptionHandler} y convertida en HTTP 400.
 * </p>
 */
package com.solare.exception;

/**
 * Excepción en tiempo de ejecución que indica que la petición o el estado del recurso
 * no cumple las reglas de negocio (por ejemplo, stock insuficiente, datos inconsistentes).
 * <p>
 * No modifica el flujo por sí sola: debe ser lanzada explícitamente desde la capa de servicio.
 * </p>
 */
public class BadRequestException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo para el cliente.
     *
     * @param message texto que se propagará al cuerpo de error HTTP
     */
    public BadRequestException(String message) {
        super(message);
    }
}
