/**
 * @file Modelos del carrito de compras.
 * @description Representa ítems del servidor, resumen con subtotal y líneas locales previas al login.
 * @see {@link ../services/cart.service.ts}
 * @see {@link ./product.model.ts}
 */

import { Product } from './product.model';

/** Ítem del carrito con referencia al producto y cantidad. */
export interface CartItem {
  /** ID de línea en servidor; índice local si el carrito está en `localStorage`. */
  cartItemId: number;
  quantity: number;
  product: Product;
}

/** Resumen del carrito devuelto por el API o calculado en cliente. */
export interface CartSummary {
  items: CartItem[];
  subtotalCop: number;
  note?: string | null;
}

/** Formato mínimo persistido localmente antes de autenticarse. */
export interface LocalCartLine {
  productId: number;
  quantity: number;
}
