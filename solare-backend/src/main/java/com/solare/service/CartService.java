package com.solare.service;

import com.solare.dto.cart.AddToCartRequest;
import com.solare.dto.cart.CartItemDto;
import com.solare.dto.cart.CartSummaryDto;
import com.solare.dto.cart.UpdateCartItemRequest;
import com.solare.exception.BadRequestException;
import com.solare.exception.ResourceNotFoundException;
import com.solare.model.entity.CartEntity;
import com.solare.model.entity.ProductEntity;
import com.solare.model.entity.UserEntity;
import com.solare.repository.CartRepository;
import com.solare.repository.ProductRepository;
import com.solare.repository.UserRepository;
import com.solare.security.SolareUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final PricingService pricingService;

    @Transactional(readOnly = true)
    public CartSummaryDto getCart(SolareUserDetails user) {
        UserEntity u = loadUser(user.getId());
        List<CartEntity> rows = cartRepository.findByUser(u);
        List<CartItemDto> items = rows.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
        BigDecimal sub = BigDecimal.ZERO;
        for (CartEntity row : rows) {
            sub = sub.add(pricingService.lineSubtotal(row.getProduct(), row.getQuantity()));
        }
        return CartSummaryDto.builder()
                .items(items)
                .subtotalCop(sub.setScale(2, RoundingMode.HALF_UP))
                .note(null)
                .build();
    }

    @Transactional
    public CartSummaryDto add(SolareUserDetails user, AddToCartRequest req) {
        UserEntity u = loadUser(user.getId());
        ProductEntity p = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        if (p.getStock() < req.getQuantity()) {
            throw new BadRequestException("Stock insuficiente");
        }
        CartEntity line = cartRepository.findByUserAndProduct(u, p).orElse(null);
        if (line == null) {
            line = CartEntity.builder().user(u).product(p).quantity(req.getQuantity()).build();
        } else {
            int next = line.getQuantity() + req.getQuantity();
            if (next > p.getStock()) {
                throw new BadRequestException("Stock insuficiente");
            }
            line.setQuantity(next);
        }
        cartRepository.save(line);
        return getCart(user);
    }

    @Transactional
    public CartSummaryDto update(SolareUserDetails user, Long cartItemId, UpdateCartItemRequest req) {
        UserEntity u = loadUser(user.getId());
        CartEntity line = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Ítem no encontrado"));
        if (!line.getUser().getId().equals(u.getId())) {
            throw new BadRequestException("Ítem no pertenece al usuario");
        }
        if (req.getQuantity() > line.getProduct().getStock()) {
            throw new BadRequestException("Stock insuficiente");
        }
        line.setQuantity(req.getQuantity());
        cartRepository.save(line);
        return getCart(user);
    }

    @Transactional
    public CartSummaryDto remove(SolareUserDetails user, Long cartItemId) {
        UserEntity u = loadUser(user.getId());
        CartEntity line = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Ítem no encontrado"));
        if (!line.getUser().getId().equals(u.getId())) {
            throw new BadRequestException("Ítem no pertenece al usuario");
        }
        cartRepository.delete(line);
        return getCart(user);
    }

    @Transactional
    public void clear(SolareUserDetails user) {
        UserEntity u = loadUser(user.getId());
        cartRepository.deleteByUser(u);
    }

    private UserEntity loadUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private CartItemDto toItemDto(CartEntity row) {
        return CartItemDto.builder()
                .cartItemId(row.getId())
                .quantity(row.getQuantity())
                .product(productMapper.toDto(row.getProduct()))
                .build();
    }
}
