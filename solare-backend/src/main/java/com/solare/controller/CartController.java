/**
 * API REST del carrito de compras (usuario autenticado).
 * <p>
 * Relación: {@link com.solare.service.CartService}.
 * </p>
 */
package com.solare.controller;

import com.solare.dto.cart.AddToCartRequest;
import com.solare.dto.cart.CartSummaryDto;
import com.solare.dto.cart.UpdateCartItemRequest;
import com.solare.security.SolareUserDetails;
import com.solare.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Operaciones del carrito del usuario autenticado bajo {@code /cart}.
 */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Carrito")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    /** Devuelve ítems y subtotal del carrito actual. */
    @GetMapping
    @Operation(summary = "Ver carrito")
    public ResponseEntity<CartSummaryDto> get(@AuthenticationPrincipal SolareUserDetails user) {
        return ResponseEntity.ok(cartService.getCart(user));
    }

    /** Agrega o incrementa cantidad de un producto en el carrito. */
    @PostMapping("/items")
    @Operation(summary = "Agregar al carrito")
    public ResponseEntity<CartSummaryDto> add(
            @AuthenticationPrincipal SolareUserDetails user,
            @Valid @RequestBody AddToCartRequest req) {
        return ResponseEntity.ok(cartService.add(user, req));
    }

    /** Alias compatible con clientes que esperan POST /cart/add. */
    @PostMapping("/add")
    @Operation(summary = "Agregar al carrito (alias de POST /cart/items)")
    public ResponseEntity<CartSummaryDto> addAlias(
            @AuthenticationPrincipal SolareUserDetails user,
            @Valid @RequestBody AddToCartRequest req) {
        return ResponseEntity.ok(cartService.add(user, req));
    }

    /** Actualiza la cantidad de un ítem existente del carrito. */
    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Actualizar cantidad")
    public ResponseEntity<CartSummaryDto> update(
            @AuthenticationPrincipal SolareUserDetails user,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest req) {
        return ResponseEntity.ok(cartService.update(user, cartItemId, req));
    }

    /** Elimina un ítem del carrito por su identificador de línea. */
    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Eliminar ítem")
    public ResponseEntity<CartSummaryDto> remove(
            @AuthenticationPrincipal SolareUserDetails user,
            @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.remove(user, cartItemId));
    }
}
