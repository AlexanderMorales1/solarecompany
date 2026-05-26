/**
 * Repositorios Spring Data JPA para pedidos.
 * <p>
 * Soporta historial por usuario y consultas con especificaciones para paneles de administración.
 */
package com.solare.repository;

import com.solare.model.entity.OrderEntity;
import com.solare.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Acceso a datos de {@link OrderEntity}.
 */
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

    /**
     * Historial de pedidos de un usuario, del más reciente al más antiguo.
     *
     * @param user comprador
     * @return pedidos ordenados por {@link OrderEntity#getCreatedAt} descendente
     */
    List<OrderEntity> findByUserOrderByCreatedAtDesc(UserEntity user);
}
