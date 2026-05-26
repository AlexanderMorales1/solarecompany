/**
 * DTOs de pedidos: checkout y creación de pedido.
 * <p>
 * Captura método de pago y datos de envío; el total y las líneas se derivan del carrito
 * en la capa de servicio al confirmar la compra.
 */
package com.solare.dto.order;

import com.solare.model.entity.OrderEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Cuerpo de petición para confirmar checkout y crear un pedido.
 */
@Data
public class CreateOrderRequest {

    /** Pasarela o forma de pago elegida. */
    @NotNull
    private OrderEntity.PaymentMethod paymentMethod;

    /** Nombre completo del comprador. */
    @NotBlank
    @Size(max = 200)
    private String fullName;

    /** Correo de contacto y notificaciones. */
    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    /** Teléfono; admite dígitos, espacios, guiones y prefijo {@code +}. */
    @NotBlank
    @Pattern(regexp = "^[+]?[0-9\\s\\-]{10,20}$", message = "Teléfono debe tener entre 10 y 20 caracteres (números, espacios o guiones)")
    private String phoneNumber;

    /** País de envío. */
    @NotBlank
    @Size(max = 100)
    private String country;

    /** Departamento o estado. */
    @NotBlank
    @Size(max = 100)
    private String department;

    /** Ciudad. */
    @NotBlank
    @Size(max = 100)
    private String city;

    /** Barrio o localidad. */
    @NotBlank
    @Size(max = 120)
    private String neighborhood;

    /** Dirección postal. */
    @NotBlank
    @Size(max = 500)
    private String address;
}
