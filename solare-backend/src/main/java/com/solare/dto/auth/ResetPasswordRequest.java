/**
 * DTOs de autenticación: confirmación de nueva contraseña.
 * <p>
 * Consumido junto al token emitido en forgot-password para actualizar el hash.
 */
package com.solare.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Cuerpo para fijar una nueva contraseña usando un token válido.
 */
@Data
public class ResetPasswordRequest {

    /** Token de un solo uso recibido por correo. */
    @NotBlank
    private String token;

    /** Nueva contraseña del usuario. */
    @NotBlank
    @Size(min = 8, max = 128)
    private String newPassword;
}
