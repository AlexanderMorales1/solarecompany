/**
 * Cálculo de subtotales de línea aplicando promociones activas (2x1 y porcentaje).
 * <p>
 * Relación: usado por {@link CartService} y {@link OrderService} en checkout.
 * </p>
 */
package com.solare.service;

import com.solare.model.entity.DiscountEntity;
import com.solare.model.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

/**
 * Reglas de precio por producto y cantidad; no persiste estado.
 */
@Service
public class PricingService {

    /**
     * Precio de una línea según cantidad y promoción 2x1 (paga ceil(qty/2) unidades al precio unitario).
     *
     * @param product  producto con posible descuento asociado
     * @param quantity unidades en la línea
     * @return subtotal en COP con escala 2
     */
    public BigDecimal lineSubtotal(ProductEntity product, int quantity) {
        if (quantity <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal unit = product.getPriceCop();
        DiscountEntity d = product.getDiscount();
        if (d != null && discountActive(d) && d.getType() == DiscountEntity.DiscountType.BOGO_TWO_FOR_ONE) {
            // 2x1: unidades pagadas = mitad superior entera (ej. 3 → paga 2)
            int paidUnits = (quantity + 1) / 2;
            return unit.multiply(BigDecimal.valueOf(paidUnits)).setScale(2, RoundingMode.HALF_UP);
        }
        if (d != null && discountActive(d) && d.getType() == DiscountEntity.DiscountType.PERCENTAGE
                && d.getValuePercent() != null) {
            BigDecimal factor = BigDecimal.ONE.subtract(
                    d.getValuePercent().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            return unit.multiply(BigDecimal.valueOf(quantity)).multiply(factor).setScale(2, RoundingMode.HALF_UP);
        }
        return unit.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Comprueba bandera activa y ventana temporal {@code startsAt}/{@code endsAt}.
     */
    private boolean discountActive(DiscountEntity d) {
        if (!d.isActive()) {
            return false;
        }
        Instant now = Instant.now();
        if (d.getStartsAt() != null && now.isBefore(d.getStartsAt())) {
            return false;
        }
        if (d.getEndsAt() != null && now.isAfter(d.getEndsAt())) {
            return false;
        }
        return true;
    }
}
