import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PageResponse, Product } from '../models/product.model';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly http = inject(HttpClient);

  list(params: {
    brand?: string;
    gender?: string;
    type?: string;
    category?: string;
    featured?: boolean;
    page?: number;
    size?: number;
  }): Observable<PageResponse<Product>> {
    let hp = new HttpParams();
    if (params.brand) hp = hp.set('brand', params.brand);
    if (params.gender) hp = hp.set('gender', params.gender);
    if (params.type) hp = hp.set('type', params.type);
    if (params.category) hp = hp.set('category', params.category);
    if (params.featured !== undefined) hp = hp.set('featured', String(params.featured));
    hp = hp.set('page', String(params.page ?? 0));
    hp = hp.set('size', String(params.size ?? 12));
    return this.http.get<PageResponse<Product>>(`${environment.apiUrl}/products`, { params: hp });
  }

  get(id: number): Observable<Product> {
    return this.http.get<Product>(`${environment.apiUrl}/products/${id}`);
  }

  featuredByBrand(brand: 'FERRATI' | 'RAYBAN'): Observable<Product[]> {
    return this.http.get<Product[]>(`${environment.apiUrl}/products/featured/${brand}`);
  }
}
