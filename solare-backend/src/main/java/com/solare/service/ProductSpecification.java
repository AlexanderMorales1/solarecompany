/**
 * Criterios dinámicos JPA para búsqueda y filtrado del catálogo de productos.
 * <p>
 * Relación: combinadas en {@link ProductService#search}.
 * </p>
 */
package com.solare.service;

import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.ProductEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * Fábrica de {@link Specification} para {@link ProductEntity}.
 */
public final class ProductSpecification {

    /** Constructor privado: clase de utilidad sin instancias. */
    private ProductSpecification() {
    }

    /** Filtro por código de marca; sin efecto si {@code code} es nulo. */
    public static Specification<ProductEntity> brandCode(BrandEntity.BrandCode code) {
        return (root, q, cb) -> code == null ? cb.conjunction()
                : cb.equal(root.get("brand").get("code"), code);
    }

    /** Filtro por público objetivo (género del catálogo). */
    public static Specification<ProductEntity> gender(ProductEntity.GenderTarget g) {
        return (root, q, cb) -> g == null ? cb.conjunction() : cb.equal(root.get("gender"), g);
    }

    /** Filtro por tipo de producto (prenda, accesorio, etc.). */
    public static Specification<ProductEntity> productType(ProductEntity.ProductType t) {
        return (root, q, cb) -> t == null ? cb.conjunction() : cb.equal(root.get("productType"), t);
    }

    /**
     * Productos que pertenecen a la categoría con el slug indicado (join interno).
     */
    public static Specification<ProductEntity> categorySlug(String slug) {
        return (root, q, cb) -> {
            if (slug == null || slug.isBlank()) {
                return cb.conjunction();
            }
            Join<Object, Object> cats = root.join("categories", JoinType.INNER);
            return cb.equal(cats.get("slug"), slug);
        };
    }

    /** Filtro por bandera de producto destacado. */
    public static Specification<ProductEntity> featured(Boolean featured) {
        return (root, q, cb) -> featured == null ? cb.conjunction() : cb.equal(root.get("featured"), featured);
    }

    /**
     * Búsqueda de texto en nombre, descripción, marca y categorías (OR de predicados).
     * <p>
     * Activa {@code distinct} porque los joins a categorías pueden duplicar filas.
     * </p>
     */
    public static Specification<ProductEntity> searchText(String query) {
        return (root, q, cb) -> {
            if (!StringUtils.hasText(query)) {
                return cb.conjunction();
            }
            q.distinct(true);
            String like = "%" + query.trim().toLowerCase() + "%";
            Join<Object, Object> brandJoin = root.join("brand", JoinType.LEFT);
            Join<Object, Object> categoryJoin = root.join("categories", JoinType.LEFT);
            Predicate byName = cb.like(cb.lower(root.get("name")), like);
            Predicate byDescription = cb.like(cb.lower(root.get("description")), like);
            Predicate byBrandCode = cb.like(cb.lower(brandJoin.get("code").as(String.class)), like);
            Predicate byBrandName = cb.like(cb.lower(brandJoin.get("displayName")), like);
            Predicate byCategorySlug = cb.like(cb.lower(categoryJoin.get("slug")), like);
            Predicate byCategoryName = cb.like(cb.lower(categoryJoin.get("name")), like);
            return cb.or(byName, byDescription, byBrandCode, byBrandName, byCategorySlug, byCategoryName);
        };
    }
}
