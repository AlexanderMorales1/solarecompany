/**
 * Entidades JPA del dominio de usuarios y autenticación.
 * <p>
 * {@link UserEntity} representa la tabla {@code users} y se relaciona con {@link RoleEntity}
 * mediante la tabla intermedia {@code user_roles}. Es consumida por repositorios en
 * {@code com.solare.repository}, servicios de autenticación y el carrito ({@link CartEntity}).
 */
package com.solare.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad de usuario registrado en la plataforma (credenciales locales u OAuth).
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    /** Identificador interno generado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Correo electrónico único; usado como nombre de usuario en login. */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /** Hash de contraseña; puede ser {@code null} si el acceso es solo por proveedor externo. */
    @Column(length = 255)
    private String password;

    /** Nombre de pila del usuario. */
    @Column(name = "first_name", length = 100)
    private String firstName;

    /** Apellido del usuario. */
    @Column(name = "last_name", length = 100)
    private String lastName;

    /** Identificador del proveedor OAuth (por ejemplo {@code google}). */
    @Column(length = 32)
    private String provider;

    /** ID del usuario en el proveedor OAuth. */
    @Column(name = "provider_id", length = 255)
    private String providerId;

    /** Token temporal para restablecer contraseña. */
    @Column(name = "reset_token", length = 255)
    private String resetToken;

    /** Fecha y hora de expiración del token de restablecimiento. */
    @Column(name = "reset_token_expiry")
    private Instant resetTokenExpiry;

    /** Indica si la cuenta está habilitada para iniciar sesión. */
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * Roles asignados al usuario; carga eager para autorización en cada petición autenticada.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();
}
