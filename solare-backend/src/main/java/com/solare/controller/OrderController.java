/**
 * API REST de pedidos del cliente autenticado (checkout e historial).
 * <p>
 * Relación: {@link com.solare.service.OrderService}. Requiere JWT en todas las rutas.
 * </p>
 */
package com.solare.controller;

import com.solare.dto.order.CreateOrderRequest;
import com.solare.dto.order.OrderDto;
import com.solare.security.SolareUserDetails;
import com.solare.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Checkout e historial de pedidos del cliente autenticado ({@code /orders}).
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    /** Confirma la compra desde el carrito y crea el pedido. */
    @PostMapping("/checkout")
    @Operation(summary = "Confirmar compra desde el carrito")
    public ResponseEntity<OrderDto> checkout(
            @AuthenticationPrincipal SolareUserDetails user,
            @Valid @RequestBody CreateOrderRequest req) {
        return ResponseEntity.ok(orderService.checkout(user, req));
    }

    /** Historial de pedidos del usuario autenticado. */
    @GetMapping("/mine")
    @Operation(summary = "Mis pedidos")
    public ResponseEntity<List<OrderDto>> mine(@AuthenticationPrincipal SolareUserDetails user) {
        return ResponseEntity.ok(orderService.myOrders(user));
    }
}
