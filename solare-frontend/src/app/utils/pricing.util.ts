/**
 * @file Utilidades de cálculo de precios en COP.
 * @description Réplica en cliente de reglas de descuento del backend (2x1 y porcentaje)
 *   para carrito local y vistas sin round-trip extra.
 * @see {@link ../models/product.model.ts} Campos `discountType` y `priceCop`.
 * @see {@link ../services/cart.service.ts} `readLocalSummary` usa `cartSubtotal`.
 */

import { Product } from '../models/product.model';

/**
 * Subtotal de una línea según tipo de descuento activo.
 * Alineado con backend: 2x1 cobra `ceil(qty/2)` al precio unitario; % si aplica.
 *
 * @param product Producto con precio y metadatos de descuento.
 * @param quantity Unidades en la línea.
 * @returns Subtotal en COP redondeado a dos decimales.
 */
export function lineSubtotalCop(product: Product, quantity: number): number {
  if (quantity <= 0) return 0;
  const unit = Number(product.priceCop);
  const d = product.discountType;
  const active = !!product.discountId && !!d;
  if (active && d === 'BOGO_TWO_FOR_ONE') {
    // 2x1: solo se pagan las unidades resultantes de dividir la cantidad entre dos (redondeo arriba).
    const paidUnits = Math.ceil(quantity / 2);
    return Math.round(unit * paidUnits * 100) / 100;
  }
  if (active && d === 'PERCENTAGE') {
    // Sin porcentaje en el DTO público; se asume precio base ya ajustado o precio × cantidad.
    return Math.round(unit * quantity * 100) / 100;
  }
  return Math.round(unit * quantity * 100) / 100;
}

/**
 * Suma subtotales de todas las líneas del carrito.
 *
 * @param items Colección con producto y cantidad por línea.
 * @returns Subtotal total en COP.
 */
export function cartSubtotal(items: { product: Product; quantity: number }[]): number {
  return (
    Math.round(
      items.reduce((s, i) => s + lineSubtotalCop(i.product, i.quantity), 0) * 100,
    ) / 100
  );
}
