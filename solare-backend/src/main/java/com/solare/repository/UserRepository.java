/**
 * Repositorios Spring Data JPA para usuarios.
 * <p>
 * Expone consultas por email, token de restablecimiento y credenciales OAuth.
 * Consumido por servicios de autenticación y registro en la capa {@code com.solare.service}.
 */
package com.solare.repository;

import com.solare.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a datos de {@link UserEntity}.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo único del usuario
     * @return usuario encontrado o vacío si no existe
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Busca un usuario por el token de restablecimiento de contraseña.
     *
     * @param resetToken token emitido en el flujo forgot-password
     * @return usuario asociado al token o vacío
     */
    Optional<UserEntity> findByResetToken(String resetToken);

    /**
     * Busca un usuario registrado vía proveedor OAuth.
     *
     * @param provider   identificador del proveedor (por ejemplo {@code google})
     * @param providerId ID del usuario en el proveedor
     * @return usuario vinculado o vacío
     */
    Optional<UserEntity> findByProviderAndProviderId(String provider, String providerId);
}
