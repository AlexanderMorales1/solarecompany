package com.solare.service;

import com.solare.dto.order.CreateOrderRequest;
import com.solare.dto.order.OrderDto;
import com.solare.dto.order.OrderLineDto;
import com.solare.exception.BadRequestException;
import com.solare.exception.ResourceNotFoundException;
import com.solare.model.entity.CartEntity;
import com.solare.model.entity.OrderDetailEntity;
import com.solare.model.entity.OrderEntity;
import com.solare.model.entity.UserEntity;
import com.solare.repository.CartRepository;
import com.solare.repository.OrderRepository;
import com.solare.repository.UserRepository;
import com.solare.security.SolareUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PricingService pricingService;

    @Transactional
    public OrderDto checkout(SolareUserDetails user, CreateOrderRequest req) {
        UserEntity u = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        List<CartEntity> lines = cartRepository.findByUser(u);
        if (lines.isEmpty()) {
            throw new BadRequestException("El carrito está vacío");
        }
        BigDecimal total = BigDecimal.ZERO;
        List<OrderDetailEntity> details = new ArrayList<>();
        OrderEntity order = OrderEntity.builder()
                .user(u)
                .totalCop(BigDecimal.ZERO)
                .status(OrderEntity.OrderStatus.PAID)
                .paymentMethod(req.getPaymentMethod())
                .customerFullName(req.getFullName().trim())
                .customerEmail(req.getEmail().trim())
                .customerPhone(req.getPhoneNumber().trim())
                .country(req.getCountry().trim())
                .department(req.getDepartment().trim())
                .city(req.getCity().trim())
                .neighborhood(req.getNeighborhood().trim())
                .address(req.getAddress().trim())
                .createdAt(Instant.now())
                .details(new ArrayList<>())
                .build();
        for (CartEntity line : lines) {
            int qty = line.getQuantity();
            if (qty > line.getProduct().getStock()) {
                throw new BadRequestException("Stock insuficiente para " + line.getProduct().getName());
            }
            BigDecimal lineTotal = pricingService.lineSubtotal(line.getProduct(), qty);
            total = total.add(lineTotal);
            OrderDetailEntity od = OrderDetailEntity.builder()
                    .order(order)
                    .product(line.getProduct())
                    .quantity(qty)
                    .unitPriceCop(line.getProduct().getPriceCop())
                    .build();
            details.add(od);
            line.getProduct().setStock(line.getProduct().getStock() - qty);
        }
        order.setTotalCop(total.setScale(2, RoundingMode.HALF_UP));
        order.getDetails().addAll(details);
        orderRepository.save(order);
        cartRepository.deleteByUser(u);
        return toDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> myOrders(SolareUserDetails user) {
        UserEntity u = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return orderRepository.findByUserOrderByCreatedAtDesc(u).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> adminOrders(String customer, OrderEntity.OrderStatus status, LocalDate from, LocalDate to, Pageable pageable) {
        Instant fromInstant = from != null ? from.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;
        Instant toExclusive = to != null ? to.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant() : null;

        Set<OrderEntity.OrderStatus> approvedStatuses = Set.of(OrderEntity.OrderStatus.PAID, OrderEntity.OrderStatus.SHIPPED);
        Set<OrderEntity.OrderStatus> statuses = status != null
                ? (approvedStatuses.contains(status) ? Set.of(status) : Set.of())
                : approvedStatuses;

        Specification<OrderEntity> spec = Specification
                .where(OrderSpecification.statusIn(statuses))
                .and(OrderSpecification.customerLike(customer))
                .and(OrderSpecification.createdAtBetween(fromInstant, toExclusive));
        return orderRepository.findAll(spec, pageable).map(this::toDto);
    }

    private OrderDto toDto(OrderEntity o) {
        List<OrderLineDto> lines = o.getDetails().stream()
                .map(d -> OrderLineDto.builder()
                        .productId(d.getProduct().getId())
                        .productName(d.getProduct().getName())
                        .quantity(d.getQuantity())
                        .unitPriceCop(d.getUnitPriceCop())
                        .build())
                .collect(Collectors.toList());
        return OrderDto.builder()
                .id(o.getId())
                .totalCop(o.getTotalCop())
                .status(o.getStatus().name())
                .paymentMethod(o.getPaymentMethod() != null ? o.getPaymentMethod().name() : null)
                .customerFullName(o.getCustomerFullName())
                .customerEmail(o.getCustomerEmail())
                .customerPhone(o.getCustomerPhone())
                .country(o.getCountry())
                .department(o.getDepartment())
                .city(o.getCity())
                .neighborhood(o.getNeighborhood())
                .address(o.getAddress())
                .createdAt(o.getCreatedAt())
                .lines(lines)
                .build();
    }
}
