import { Product } from './product.model';

export interface CartItem {
  cartItemId: number;
  quantity: number;
  product: Product;
}

export interface CartSummary {
  items: CartItem[];
  subtotalCop: number;
  note?: string | null;
}

export interface LocalCartLine {
  productId: number;
  quantity: number;
}
