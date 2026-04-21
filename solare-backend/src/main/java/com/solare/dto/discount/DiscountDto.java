package com.solare.dto.discount;

import com.solare.model.entity.DiscountEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    private Long id;
    private String code;
    private String name;
    private DiscountEntity.DiscountType type;
    private BigDecimal valuePercent;
    private boolean active;
    private Instant startsAt;
    private Instant endsAt;
}
