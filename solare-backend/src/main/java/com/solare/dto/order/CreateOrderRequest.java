package com.solare.dto.order;

import com.solare.model.entity.OrderEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotNull
    private OrderEntity.PaymentMethod paymentMethod;
}
