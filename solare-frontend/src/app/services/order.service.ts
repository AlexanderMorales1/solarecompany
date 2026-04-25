import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CheckoutPayload, Order } from '../models/order.model';
import { PageResponse } from '../models/product.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly http = inject(HttpClient);

  checkout(payload: CheckoutPayload): Observable<Order> {
    return this.http.post<Order>(`${environment.apiUrl}/orders/checkout`, payload);
  }

  mine(): Observable<Order[]> {
    return this.http.get<Order[]>(`${environment.apiUrl}/orders/mine`);
  }

  adminList(params: {
    customer?: string;
    status?: string;
    from?: string;
    to?: string;
    page?: number;
    size?: number;
  }): Observable<PageResponse<Order>> {
    let hp = new HttpParams();
    if (params.customer) hp = hp.set('customer', params.customer);
    if (params.status) hp = hp.set('status', params.status);
    if (params.from) hp = hp.set('from', params.from);
    if (params.to) hp = hp.set('to', params.to);
    hp = hp.set('page', String(params.page ?? 0));
    hp = hp.set('size', String(params.size ?? 20));
    return this.http.get<PageResponse<Order>>(`${environment.apiUrl}/admin/orders`, { params: hp });
  }
}
