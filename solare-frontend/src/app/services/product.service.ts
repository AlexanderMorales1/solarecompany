/**
 * @file Servicio de catálogo de productos.
 * @description Consultas paginadas, detalle, destacados por marca y recientes.
 * @see {@link ../models/product.model.ts}
 */

import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PageResponse, Product } from '../models/product.model';

/** Acceso al API público `/products`. */
@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly http = inject(HttpClient);

  /**
   * Lista productos con filtros y paginación.
   * @param params Búsqueda, marca, género, tipo, categoría, destacados, página y orden.
   */
  list(params: {
    q?: string;
    brand?: string;
    gender?: string;
    type?: string;
    category?: string;
    featured?: boolean;
    page?: number;
    size?: number;
    sort?: string;
  }): Observable<PageResponse<Product>> {
    let hp = new HttpParams();
    if (params.q) hp = hp.set('q', params.q);
    if (params.brand) hp = hp.set('brand', params.brand);
    if (params.gender) hp = hp.set('gender', params.gender);
    if (params.type) hp = hp.set('type', params.type);
    if (params.category) hp = hp.set('category', params.category);
    if (params.featured !== undefined) hp = hp.set('featured', String(params.featured));
    hp = hp.set('page', String(params.page ?? 0));
    hp = hp.set('size', String(params.size ?? 12));
    if (params.sort) hp = hp.set('sort', params.sort);
    return this.http.get<PageResponse<Product>>(`${environment.apiUrl}/products`, { params: hp });
  }

  /** Detalle de un producto por ID. */
  get(id: number): Observable<Product> {
    return this.http.get<Product>(`${environment.apiUrl}/products/${id}`);
  }

  /** Productos destacados de una marca (Ferrati o Ray-Ban). */
  featuredByBrand(brand: 'FERRATI' | 'RAYBAN'): Observable<Product[]> {
    return this.http.get<Product[]>(`${environment.apiUrl}/products/featured/${brand}`);
  }

  /**
   * Productos más recientes (primera página).
   * @param limit Cantidad máxima de ítems (por defecto 8).
   */
  recent(limit = 8): Observable<PageResponse<Product>> {
    return this.http.get<PageResponse<Product>>(`${environment.apiUrl}/products/recent`, {
      params: new HttpParams().set('page', '0').set('size', String(limit)),
    });
  }
}
