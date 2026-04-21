package com.solare.dto.discount;

import com.solare.model.entity.DiscountEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class DiscountUpsertDto {
    @NotBlank
    @Size(max = 64)
    private String code;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    private DiscountEntity.DiscountType type;

    private BigDecimal valuePercent;

    private boolean active;

    private Instant startsAt;

    private Instant endsAt;
}
