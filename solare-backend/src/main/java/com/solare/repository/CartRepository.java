/**
 * Repositorios Spring Data JPA para el carrito de compras.
 * <p>
 * Gestiona líneas por usuario y producto; usado al añadir ítems, actualizar cantidades
 * y vaciar el carrito tras confirmar un pedido.
 */
package com.solare.repository;

import com.solare.model.entity.CartEntity;
import com.solare.model.entity.ProductEntity;
import com.solare.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de {@link CartEntity}.
 */
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    /**
     * Lista todas las líneas del carrito de un usuario.
     *
     * @param user propietario del carrito
     * @return líneas con producto y cantidad
     */
    List<CartEntity> findByUser(UserEntity user);

    /**
     * Busca la línea existente para un par usuario-producto.
     *
     * @param user    propietario del carrito
     * @param product producto en el carrito
     * @return línea única o vacío si el producto no está en el carrito
     */
    Optional<CartEntity> findByUserAndProduct(UserEntity user, ProductEntity product);

    /**
     * Elimina todas las líneas del carrito del usuario.
     * <p>
     * Efecto secundario: borrado en base de datos; no modifica inventario ni pedidos.
     *
     * @param user propietario cuyo carrito se vacía
     */
    void deleteByUser(UserEntity user);
}
