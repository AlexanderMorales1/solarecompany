/**
 * Repositorios Spring Data JPA para roles de seguridad.
 * <p>
 * Permite resolver {@link com.solare.model.entity.RoleEntity} por nombre al asignar roles
 * en registro o administración de usuarios.
 */
package com.solare.repository;

import com.solare.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a datos de {@link RoleEntity}.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Obtiene el rol persistido que coincide con el nombre enumerado.
     *
     * @param name valor de {@link RoleEntity.RoleName}
     * @return entidad de rol o vacío si no está sembrada en BD
     */
    Optional<RoleEntity> findByName(RoleEntity.RoleName name);
}
