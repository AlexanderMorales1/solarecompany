package com.solare.service;

import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.ProductEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<ProductEntity> brandCode(BrandEntity.BrandCode code) {
        return (root, q, cb) -> code == null ? cb.conjunction()
                : cb.equal(root.get("brand").get("code"), code);
    }

    public static Specification<ProductEntity> gender(ProductEntity.GenderTarget g) {
        return (root, q, cb) -> g == null ? cb.conjunction() : cb.equal(root.get("gender"), g);
    }

    public static Specification<ProductEntity> productType(ProductEntity.ProductType t) {
        return (root, q, cb) -> t == null ? cb.conjunction() : cb.equal(root.get("productType"), t);
    }

    public static Specification<ProductEntity> categorySlug(String slug) {
        return (root, q, cb) -> {
            if (slug == null || slug.isBlank()) {
                return cb.conjunction();
            }
            Join<Object, Object> cats = root.join("categories", JoinType.INNER);
            return cb.equal(cats.get("slug"), slug);
        };
    }

    public static Specification<ProductEntity> featured(Boolean featured) {
        return (root, q, cb) -> featured == null ? cb.conjunction() : cb.equal(root.get("featured"), featured);
    }
}
