/**
 * Mapeo de {@link com.solare.model.entity.ProductEntity} a {@link com.solare.dto.product.ProductDto}.
 * <p>
 * Normaliza URLs de imágenes relativas al prefijo público de la API local.
 * </p>
 */
package com.solare.service;

import com.solare.dto.product.ProductDto;
import com.solare.model.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/** Componente Spring sin estado para transformación entidad → DTO. */
@Component
public class ProductMapper {

    /**
     * Convierte producto a DTO; rutas de imagen no absolutas se prefijan con el host de desarrollo.
     *
     * @param p entidad JPA (puede ser null)
     * @return DTO o null si la entidad es null
     */
    public ProductDto toDto(ProductEntity p) {
        if (p == null) {
            return null;
        }
        return ProductDto.builder()
                .id(p.getId())
                .name(p.getName())
                .brandCode(p.getBrand() != null ? p.getBrand().getCode().name() : null)
                .brandDisplayName(p.getBrand() != null ? p.getBrand().getDisplayName() : null)
                .productType(p.getProductType() != null ? p.getProductType().name() : null)
                .gender(p.getGender() != null ? p.getGender().name() : null)
                .priceCop(p.getPriceCop())
                .description(p.getDescription())
                .stock(p.getStock())
                .featured(p.isFeatured())
                .discountId(p.getDiscount() != null ? p.getDiscount().getId() : null)
                .discountCode(p.getDiscount() != null ? p.getDiscount().getCode() : null)
                .discountType(p.getDiscount() != null ? p.getDiscount().getType().name() : null)
                .imageUrls(
                        p.getImageUrls() != null
                                ? p.getImageUrls().stream()
                                .map(img -> {
                                    // URLs ya absolutas (CDN o externas) se dejan intactas
                                    if (img.startsWith("http")) {
                                        return img;
                                    }
                                    return "http://localhost:8080/api" + img;
                                })
                                .toList()
                                : java.util.List.of()
                )
                .categorySlugs(p.getCategories() != null
                        ? p.getCategories().stream().map(c -> c.getSlug()).collect(Collectors.toSet())
                        : java.util.Set.of())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
