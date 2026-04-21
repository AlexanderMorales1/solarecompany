package com.solare.controller;

import com.solare.dto.discount.DiscountDto;
import com.solare.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/discounts/public")
@RequiredArgsConstructor
@Tag(name = "Promociones (público)")
public class DiscountPublicController {

    private final DiscountService discountService;

    @GetMapping
    @Operation(summary = "Promociones activas")
    public ResponseEntity<List<DiscountDto>> active() {
        return ResponseEntity.ok(discountService.listActive());
    }
}
