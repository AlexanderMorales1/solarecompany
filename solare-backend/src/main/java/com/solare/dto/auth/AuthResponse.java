/**
 * DTOs de autenticación: respuesta tras login o registro exitoso.
 * <p>
 * Incluye el JWT y metadatos del usuario para el cliente Angular.
 */
package com.solare.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Respuesta con token de acceso y perfil resumido del usuario autenticado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /** Token JWT firmado. */
    private String token;

    /** Esquema del token (típicamente {@code Bearer}). */
    private String type;

    /** Identificador del usuario. */
    private Long userId;

    /** Correo del usuario. */
    private String email;

    /** Nombre de pila. */
    private String firstName;

    /** Apellido. */
    private String lastName;

    /** Nombres de roles (por ejemplo {@code ROLE_USER}). */
    private Set<String> roles;
}
