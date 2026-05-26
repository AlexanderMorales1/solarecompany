/**
 * @file Modelo de categoría de producto.
 * @description Usado en filtros del catálogo y formularios del panel admin.
 * @see {@link ../services/category.service.ts}
 */

/** Categoría con slug para filtros y asociación en productos. */
export interface Category {
  id: number;
  name: string;
  slug: string;
}
