import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, Subject, concatMap, from, last, map, of, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { CartSummary } from '../models/cart.model';
import { Product } from '../models/product.model';
import { cartSubtotal } from '../utils/pricing.util';
import { AuthService } from './auth.service';

const LOCAL_KEY = 'solare_local_cart';

export interface LocalCartEntry {
  product: Product;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly http = inject(HttpClient);
  private readonly auth = inject(AuthService);

  private readonly cartChangedSubject = new Subject<void>();

  /** Emite cuando el contenido del carrito cambia (local o servidor). */
  readonly cartChanged = this.cartChangedSubject.asObservable();

  private notifyCartChanged(): void {
    this.cartChangedSubject.next();
  }

  getCart(): Observable<CartSummary> {
    if (this.auth.isLoggedIn()) {
      return this.http.get<CartSummary>(`${environment.apiUrl}/cart`);
    }
    return of(this.readLocalSummary());
  }

  addProduct(product: Product, quantity: number): Observable<CartSummary> {
    if (this.auth.isLoggedIn()) {
      return this.http
        .post<CartSummary>(`${environment.apiUrl}/cart/items`, {
          productId: product.id,
          quantity,
        })
        .pipe(tap(() => this.notifyCartChanged()));
    }
    this.upsertLocal(product, quantity);
    this.notifyCartChanged();
    return of(this.readLocalSummary());
  }

  updateQuantity(cartItemId: number, quantity: number): Observable<CartSummary> {
    if (this.auth.isLoggedIn()) {
      return this.http
        .put<CartSummary>(`${environment.apiUrl}/cart/items/${cartItemId}`, {
          quantity,
        })
        .pipe(tap(() => this.notifyCartChanged()));
    }
    const entries = this.readLocal();
    const idx = entries.findIndex((_, i) => i === cartItemId);
    if (idx >= 0 && entries[idx]) {
      entries[idx].quantity = quantity;
      this.writeLocal(entries);
    }
    this.notifyCartChanged();
    return of(this.readLocalSummary());
  }

  removeLine(cartItemId: number): Observable<CartSummary> {
    if (this.auth.isLoggedIn()) {
      return this.http
        .delete<CartSummary>(`${environment.apiUrl}/cart/items/${cartItemId}`)
        .pipe(tap(() => this.notifyCartChanged()));
    }
    const entries = this.readLocal().filter((_, i) => i !== cartItemId);
    this.writeLocal(entries);
    this.notifyCartChanged();
    return of(this.readLocalSummary());
  }

  /** Tras login: sube ítems locales al carrito del servidor. */
  mergeLocalToServer(): Observable<void> {
    const entries = this.readLocal();
    if (!this.auth.isLoggedIn() || entries.length === 0) {
      return of(undefined);
    }
    return from(entries).pipe(
      concatMap((e) =>
        this.http.post<CartSummary>(`${environment.apiUrl}/cart/items`, {
          productId: e.product.id,
          quantity: e.quantity,
        }),
      ),
      last(),
      tap(() => this.clearLocal()),
      tap(() => this.notifyCartChanged()),
      map(() => undefined),
    );
  }

  private readLocal(): LocalCartEntry[] {
    try {
      const raw = localStorage.getItem(LOCAL_KEY);
      if (!raw) return [];
      return JSON.parse(raw) as LocalCartEntry[];
    } catch {
      return [];
    }
  }

  private writeLocal(entries: LocalCartEntry[]): void {
    localStorage.setItem(LOCAL_KEY, JSON.stringify(entries));
  }

  private clearLocal(): void {
    localStorage.removeItem(LOCAL_KEY);
  }

  private upsertLocal(product: Product, quantity: number): void {
    const entries = this.readLocal();
    const found = entries.find((e) => e.product.id === product.id);
    if (found) {
      found.quantity += quantity;
      if (found.quantity > product.stock) found.quantity = product.stock;
    } else {
      entries.push({ product: { ...product }, quantity });
    }
    this.writeLocal(entries);
  }

  private readLocalSummary(): CartSummary {
    const entries = this.readLocal();
    const items = entries.map((e, index) => ({
      cartItemId: index,
      quantity: e.quantity,
      product: e.product,
    }));
    const subtotalCop = cartSubtotal(entries.map((e) => ({ product: e.product, quantity: e.quantity })));
    return { items, subtotalCop };
  }
}
