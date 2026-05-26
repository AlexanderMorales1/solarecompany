/**
 * @file Servicio de pedidos.
 * @description Checkout del usuario, historial propio y listado paginado para administradores.
 * @see {@link ../models/order.model.ts}
 */

import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CheckoutPayload, Order } from '../models/order.model';
import { PageResponse } from '../models/product.model';

/** Cliente HTTP para endpoints `/orders` y `/admin/orders`. */
@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly http = inject(HttpClient);

  /** Crea un pedido a partir del carrito y datos de envío/pago. */
  checkout(payload: CheckoutPayload): Observable<Order> {
    return this.http.post<Order>(`${environment.apiUrl}/orders/checkout`, payload);
  }

  /** Lista los pedidos del usuario autenticado. */
  mine(): Observable<Order[]> {
    return this.http.get<Order[]>(`${environment.apiUrl}/orders/mine`);
  }

  /**
   * Lista pedidos con filtros (panel admin).
   * @param params Filtro por cliente, estado, rango de fechas y paginación.
   */
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
