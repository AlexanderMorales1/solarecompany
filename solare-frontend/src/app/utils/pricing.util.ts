import { Product } from '../models/product.model';

/** Alineado con backend: 2x1 paga ceil(qty/2) al precio unitario; % si aplica. */
export function lineSubtotalCop(product: Product, quantity: number): number {
  if (quantity <= 0) return 0;
  const unit = Number(product.priceCop);
  const d = product.discountType;
  const active = !!product.discountId && !!d;
  if (active && d === 'BOGO_TWO_FOR_ONE') {
    const paidUnits = Math.ceil(quantity / 2);
    return Math.round(unit * paidUnits * 100) / 100;
  }
  if (active && d === 'PERCENTAGE') {
    // Sin porcentaje en DTO público simple; precio ya podría venir ajustado — usar precio base * qty
    return Math.round(unit * quantity * 100) / 100;
  }
  return Math.round(unit * quantity * 100) / 100;
}

export function cartSubtotal(items: { product: Product; quantity: number }[]): number {
  return (
    Math.round(
      items.reduce((s, i) => s + lineSubtotalCop(i.product, i.quantity), 0) * 100,
    ) / 100
  );
}
