/**
 * DTOs de autenticación: perfil del usuario autenticado.
 * <p>
 * Expuesto en endpoints de cuenta sin incluir el token JWT.
 */
package com.solare.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Representación pública del perfil de un usuario logueado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    /** Identificador del usuario. */
    private Long userId;

    /** Correo electrónico. */
    private String email;

    /** Nombre de pila. */
    private String firstName;

    /** Apellido. */
    private String lastName;

    /** Roles asignados al usuario. */
    private Set<String> roles;
}
