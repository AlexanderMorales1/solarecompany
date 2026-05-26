/**
 * @file Modelos de producto y paginación.
 * @description Contratos del catálogo público y respuestas paginadas del backend.
 * @see {@link ../services/product.service.ts}
 * @see {@link ../utils/pricing.util.ts} Cálculo de subtotales con descuentos.
 */

/** Producto del catálogo con precio, stock, imágenes y categorías. */
export interface Product {
  id: number;
  name: string;
  brandCode: string;
  brandDisplayName: string;
  productType: string;
  gender: string;
  priceCop: number;
  description: string;
  stock: number;
  featured: boolean;
  discountId?: number | null;
  discountCode?: string | null;
  discountType?: string | null;
  imageUrls: string[];
  categorySlugs: string[];
  createdAt?: string;
}

/** Respuesta paginada genérica (Spring `Page`). */
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
