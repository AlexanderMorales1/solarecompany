/**
 * Entidades JPA de roles y permisos de la aplicación.
 * <p>
 * {@link RoleEntity} define los roles persistidos en {@code roles} y se asocia a
 * {@link UserEntity} vía {@code user_roles}. Los valores de {@link RoleEntity.RoleName}
 * alimentan la configuración de Spring Security en la capa de servicios.
 */
package com.solare.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rol de seguridad asignable a uno o más usuarios.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {

    /** Identificador interno del rol. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del rol almacenado como cadena (convención Spring Security {@code ROLE_*}). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 32)
    private RoleName name;

    /**
     * Catálogo fijo de roles soportados por la API.
     */
    public enum RoleName {
        /** Cliente de la tienda con acceso al catálogo y compras. */
        ROLE_USER,
        /** Administrador con operaciones de backoffice. */
        ROLE_ADMIN
    }
}
