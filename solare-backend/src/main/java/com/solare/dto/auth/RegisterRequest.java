/**
 * DTOs de autenticación: registro de nuevos usuarios.
 * <p>
 * Datos mínimos para crear una cuenta local; las reglas de validación se aplican
 * en el controlador antes de persistir {@link com.solare.model.entity.UserEntity}.
 */
package com.solare.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Cuerpo de petición para registro de usuario.
 */
@Data
public class RegisterRequest {

    /** Correo que será el identificador de la cuenta. */
    @NotBlank
    @Email
    private String email;

    /** Contraseña elegida por el usuario (longitud mínima 8). */
    @NotBlank
    @Size(min = 8, max = 128)
    private String password;

    /** Nombre de pila. */
    @NotBlank
    @Size(max = 100)
    private String firstName;

    /** Apellido. */
    @NotBlank
    @Size(max = 100)
    private String lastName;
}
