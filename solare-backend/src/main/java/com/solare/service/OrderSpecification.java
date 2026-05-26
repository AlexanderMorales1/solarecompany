/**
 * Especificaciones JPA reutilizables para filtrar pedidos en listados de administración.
 * <p>
 * Relación: consumidas por {@link OrderService#adminOrders}.
 * </p>
 */
package com.solare.service;

import com.solare.model.entity.OrderEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Set;

/**
 * Fábrica de {@link Specification} para {@link OrderEntity}; sin estado (métodos estáticos).
 */
public final class OrderSpecification {

    /** Constructor privado: clase de utilidad sin instancias. */
    private OrderSpecification() {
    }

    /**
     * Filtra por conjunto de estados; si está vacío o es nulo, no restringe.
     *
     * @param statuses estados permitidos (PAID, SHIPPED, etc.)
     */
    public static Specification<OrderEntity> statusIn(Set<OrderEntity.OrderStatus> statuses) {
        return (root, q, cb) -> (statuses == null || statuses.isEmpty())
                ? cb.conjunction()
                : root.get("status").in(statuses);
    }

    /**
     * Búsqueda parcial insensible a mayúsculas en nombre o correo del cliente del pedido.
     *
     * @param customer texto libre (opcional)
     */
    public static Specification<OrderEntity> customerLike(String customer) {
        return (root, q, cb) -> {
            if (!StringUtils.hasText(customer)) {
                return cb.conjunction();
            }
            String like = "%" + customer.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("customerFullName")), like),
                    cb.like(cb.lower(root.get("customerEmail")), like)
            );
        };
    }

    /**
     * Rango de fechas de creación: {@code toExclusive} es límite superior exclusivo (inicio del día siguiente).
     *
     * @param from         instante inicial inclusive (opcional)
     * @param toExclusive  instante final exclusive (opcional)
     */
    public static Specification<OrderEntity> createdAtBetween(Instant from, Instant toExclusive) {
        return (root, q, cb) -> {
            if (from == null && toExclusive == null) {
                return cb.conjunction();
            }
            if (from != null && toExclusive != null) {
                return cb.and(
                        cb.greaterThanOrEqualTo(root.get("createdAt"), from),
                        cb.lessThan(root.get("createdAt"), toExclusive)
                );
            }
            if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            }
            return cb.lessThan(root.get("createdAt"), toExclusive);
        };
    }
}
