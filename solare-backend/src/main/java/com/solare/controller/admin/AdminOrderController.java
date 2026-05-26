/**
 * Listado paginado de pedidos para backoffice (filtros por cliente, estado y fechas).
 */
package com.solare.controller.admin;

import com.solare.dto.order.OrderDto;
import com.solare.model.entity.OrderEntity;
import com.solare.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Listado paginado de pedidos para backoffice ({@code /admin/orders}).
 */
@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin — Pedidos")
@SecurityRequirement(name = "bearerAuth")
public class AdminOrderController {

    private final OrderService orderService;

    /**
     * Pedidos en estados PAID/SHIPPED con filtros opcionales por cliente, estado y rango de fechas.
     *
     * @param customer texto en nombre o email del cliente
     * @param status   estado del pedido (solo PAID o SHIPPED devuelven filas)
     * @param from     fecha inicial inclusive
     * @param to       fecha final inclusive
     * @param pageable paginación
     */
    @GetMapping
    @Operation(summary = "Listar pedidos aprobados")
    public ResponseEntity<Page<OrderDto>> list(
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) OrderEntity.OrderStatus status,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(orderService.adminOrders(customer, status, from, to, pageable));
    }
}
