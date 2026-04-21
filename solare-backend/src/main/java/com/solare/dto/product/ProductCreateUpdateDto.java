package com.solare.dto.product;

import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.ProductEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class ProductCreateUpdateDto {
    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    private BrandEntity.BrandCode brandCode;

    @NotNull
    private ProductEntity.ProductType productType;

    @NotNull
    private ProductEntity.GenderTarget gender;

    @NotNull
    @Min(0)
    private BigDecimal priceCop;

    @Size(max = 5000)
    private String description;

    @NotNull
    @Min(0)
    private Integer stock;

    private boolean featured;

    private Long discountId;

    private List<@NotBlank @Size(max = 1024) String> imageUrls;

    private Set<@NotBlank String> categorySlugs;
}
