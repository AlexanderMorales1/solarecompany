import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CheckoutPayload, Order } from '../models/order.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly http = inject(HttpClient);

  checkout(payload: CheckoutPayload): Observable<Order> {
    return this.http.post<Order>(`${environment.apiUrl}/orders/checkout`, payload);
  }

  mine(): Observable<Order[]> {
    return this.http.get<Order[]>(`${environment.apiUrl}/orders/mine`);
  }
}
