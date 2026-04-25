package com.solare.service;

import com.solare.model.entity.OrderEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Set;

public final class OrderSpecification {

    private OrderSpecification() {
    }

    public static Specification<OrderEntity> statusIn(Set<OrderEntity.OrderStatus> statuses) {
        return (root, q, cb) -> (statuses == null || statuses.isEmpty())
                ? cb.conjunction()
                : root.get("status").in(statuses);
    }

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
