package com.solare.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String brandCode;
    private String brandDisplayName;
    private String productType;
    private String gender;
    private BigDecimal priceCop;
    private String description;
    private int stock;
    private boolean featured;
    private Long discountId;
    private String discountCode;
    private String discountType;
    private List<String> imageUrls;
    private Set<String> categorySlugs;
}
