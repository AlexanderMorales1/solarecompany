import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HomeBanner } from '../models/banner.model';

@Injectable({ providedIn: 'root' })
export class HomeBannerService {
  private readonly http = inject(HttpClient);

  listActive(): Observable<HomeBanner[]> {
    return this.http.get<HomeBanner[]>(`${environment.apiUrl}/home-banners`);
  }
}
