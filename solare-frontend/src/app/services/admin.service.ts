/**
 * @file Servicio de operaciones de administración.
 * @description CRUD de productos y banners, y subida de imágenes al almacenamiento del backend.
 * Requiere JWT con rol admin (interceptor + adminGuard).
 * @see {@link ../pages/admin/admin.page.ts}
 */

import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HomeBanner, UploadedImage } from '../models/banner.model';
import { Product } from '../models/product.model';

/** Payload para crear o actualizar un producto en el panel admin. */
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

/** Cliente para rutas bajo `/admin/*`. */
@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly http = inject(HttpClient);

  /** Crea un producto nuevo. */
  createProduct(p: ProductUpsertPayload): Observable<Product> {
    return this.http.post<Product>(`${environment.apiUrl}/admin/products`, p);
  }

  /** Actualiza un producto existente por ID. */
  updateProduct(id: number, p: ProductUpsertPayload): Observable<Product> {
    return this.http.put<Product>(`${environment.apiUrl}/admin/products/${id}`, p);
  }

  /** Elimina un producto por ID. */
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/admin/products/${id}`);
  }

  /**
   * Sube uno o más archivos de imagen.
   * @param files Archivos a subir.
   * @param folder Carpeta destino en el servidor (`products` o `banners`).
   */
  uploadImages(files: File[], folder: 'products' | 'banners' = 'products'): Observable<UploadedImage[]> {
    const fd = new FormData();
    files.forEach((f) => fd.append('files', f));
    fd.append('folder', folder);
    return this.http.post<UploadedImage[]>(`${environment.apiUrl}/admin/media/images`, fd);
  }

  /** Lista todos los banners (activos e inactivos) para administración. */
  listBanners(): Observable<HomeBanner[]> {
    return this.http.get<HomeBanner[]>(`${environment.apiUrl}/admin/home-banners`);
  }

  /** Crea un banner con imagen y metadatos opcionales. */
  createBanner(payload: {
    imageFile: File;
    title?: string;
    subtitle?: string;
    active?: boolean;
    displayOrder?: number;
  }): Observable<HomeBanner> {
    const fd = new FormData();
    fd.append('imageFile', payload.imageFile);
    if (payload.title) fd.append('title', payload.title);
    if (payload.subtitle) fd.append('subtitle', payload.subtitle);
    if (payload.active !== undefined) fd.append('active', String(payload.active));
    if (payload.displayOrder !== undefined) fd.append('displayOrder', String(payload.displayOrder));
    return this.http.post<HomeBanner>(`${environment.apiUrl}/admin/home-banners`, fd);
  }

  /** Actualiza metadatos y opcionalmente la imagen de un banner. */
  updateBanner(
    id: number,
    payload: { title?: string; subtitle?: string; active?: boolean; displayOrder?: number; imageFile?: File },
  ): Observable<HomeBanner> {
    const fd = new FormData();
    if (payload.title !== undefined) fd.append('title', payload.title);
    if (payload.subtitle !== undefined) fd.append('subtitle', payload.subtitle);
    if (payload.active !== undefined) fd.append('active', String(payload.active));
    if (payload.displayOrder !== undefined) fd.append('displayOrder', String(payload.displayOrder));
    if (payload.imageFile) fd.append('imageFile', payload.imageFile);
    return this.http.put<HomeBanner>(`${environment.apiUrl}/admin/home-banners/${id}`, fd);
  }

  /** Elimina un banner por ID. */
  deleteBanner(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/admin/home-banners/${id}`);
  }
}
