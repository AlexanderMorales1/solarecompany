/**
 * @file Servicio público de banners de inicio.
 * @description Expone banners activos para el carrusel de la home (sin autenticación admin).
 * @see {@link ../pages/home/home.page.ts}
 * @see {@link ../models/banner.model.ts}
 */

import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HomeBanner } from '../models/banner.model';

/** Cliente para `GET /home-banners`. */
@Injectable({ providedIn: 'root' })
export class HomeBannerService {
  private readonly http = inject(HttpClient);

  /** Lista banners marcados como activos en el backend. */
  listActive(): Observable<HomeBanner[]> {
    return this.http.get<HomeBanner[]>(`${environment.apiUrl}/home-banners`);
  }
}
