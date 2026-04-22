package com.solare.dto.order;

import com.solare.model.entity.OrderEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotNull
    private OrderEntity.PaymentMethod paymentMethod;

    @NotBlank
    @Size(max = 200)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Pattern(regexp = "^[+]?[0-9\\s\\-]{10,20}$", message = "Teléfono debe tener entre 10 y 20 caracteres (números, espacios o guiones)")
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    private String country;

    @NotBlank
    @Size(max = 100)
    private String department;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 120)
    private String neighborhood;

    @NotBlank
    @Size(max = 500)
    private String address;
}
