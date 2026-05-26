/**
 * DTOs de autenticación: solicitud de restablecimiento de contraseña.
 * <p>
 * Inicia el flujo que genera {@code resetToken} en {@link com.solare.model.entity.UserEntity}.
 */
package com.solare.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Petición para enviar enlace o instrucciones de recuperación de contraseña.
 */
@Data
public class ForgotPasswordRequest {

    /** Correo de la cuenta a recuperar. */
    @NotBlank
    @Email
    private String email;
}
