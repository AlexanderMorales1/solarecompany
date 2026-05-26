/**
 * DTOs de autenticación: credenciales de inicio de sesión.
 * <p>
 * Recibido por los controladores REST de auth; validado con Bean Validation antes
 * de delegar al servicio de seguridad.
 */
package com.solare.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Cuerpo de petición para login con email y contraseña.
 */
@Data
public class LoginRequest {

    /** Correo del usuario. */
    @NotBlank
    @Email
    private String email;

    /** Contraseña en texto plano (se verifica contra el hash en servidor). */
    @NotBlank
    private String password;
}
