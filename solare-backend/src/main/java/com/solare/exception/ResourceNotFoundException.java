/**
 * Excepciones de dominio cuando un recurso solicitado no existe en persistencia.
 * <p>
 * Relación: usada en servicios al no encontrar entidades por id/slug; manejada por
 * {@link com.solare.exception.GlobalExceptionHandler} como HTTP 404.
 * </p>
 */
package com.solare.exception;

/**
 * Excepción que señala que un recurso (producto, pedido, usuario, etc.) no fue encontrado.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construye la excepción con mensaje orientado al usuario o logs.
     *
     * @param message descripción del recurso no encontrado
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
