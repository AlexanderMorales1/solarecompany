package com.solare.service;

import com.solare.dto.product.ProductDto;
import com.solare.model.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {

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
                .imageUrls(p.getImageUrls() != null ? p.getImageUrls() : java.util.List.of())
                .categorySlugs(p.getCategories() != null
                        ? p.getCategories().stream().map(c -> c.getSlug()).collect(Collectors.toSet())
                        : java.util.Set.of())
                .build();
    }
}
