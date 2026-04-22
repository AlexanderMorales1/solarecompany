package com.solare.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private BigDecimal totalCop;
    private String status;
    private String paymentMethod;
    private String customerFullName;
    private String customerEmail;
    private String customerPhone;
    private String country;
    private String department;
    private String city;
    private String neighborhood;
    private String address;
    private Instant createdAt;
    private List<OrderLineDto> lines;
}
