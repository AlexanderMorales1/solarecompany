/**
 * @file Servicio de categorías de producto.
 * @description Lista categorías públicas para filtros y formularios admin.
 * @see {@link ../models/category.model.ts}
 */

import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Category } from '../models/category.model';

/** Cliente HTTP para el endpoint `/categories`. */
@Injectable({ providedIn: 'root' })
export class CategoryService {
  private readonly http = inject(HttpClient);

  /** Obtiene todas las categorías disponibles. */
  list(): Observable<Category[]> {
    return this.http.get<Category[]>(`${environment.apiUrl}/categories`);
  }
}
