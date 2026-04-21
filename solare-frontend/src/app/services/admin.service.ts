import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Product } from '../models/product.model';

export interface ProductUpsertPayload {
  name: string;
  brandCode: 'FERRATI' | 'RAYBAN';
  productType: 'CASUAL' | 'DEPORTIVO';
  gender: 'HOMBRE' | 'MUJER';
  priceCop: number;
  description: string;
  stock: number;
  featured: boolean;
  discountId?: number | null;
  imageUrls: string[];
  categorySlugs: string[];
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly http = inject(HttpClient);

  createProduct(p: ProductUpsertPayload): Observable<Product> {
    return this.http.post<Product>(`${environment.apiUrl}/admin/products`, p);
  }

  updateProduct(id: number, p: ProductUpsertPayload): Observable<Product> {
    return this.http.put<Product>(`${environment.apiUrl}/admin/products/${id}`, p);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/admin/products/${id}`);
  }
}
