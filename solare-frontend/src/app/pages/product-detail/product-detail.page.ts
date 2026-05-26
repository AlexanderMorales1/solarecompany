/**
 * @file Página de detalle de producto.
 * @description Muestra un producto por `:id`, selector de cantidad, animación de entrada
 *   y acciones agregar al carrito / comprar ahora.
 * @see {@link ../../services/product.service.ts}
 * @see {@link ../../services/cart.service.ts}
 */

import { animate, style, transition, trigger } from '@angular/animations';
import { AsyncPipe } from '@angular/common';
import { Component, NgZone, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, map, switchMap, take, tap } from 'rxjs';
import { Product } from '../../models/product.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { CartService } from '../../services/cart.service';
import { ProductService } from '../../services/product.service';

/** Detalle de producto con scroll al contenedor principal al cargar. */
@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [AsyncPipe, CopCurrencyPipe],
  templateUrl: './product-detail.page.html',
  animations: [
    trigger('detailEnter', [
      transition('* => *', [
        style({ opacity: 0, transform: 'translateY(-20px)' }),
        animate(
          '450ms cubic-bezier(0.22, 1, 0.36, 1)',
          style({ opacity: 1, transform: 'translateY(0)' }),
        ),
      ]),
    ]),
  ],
})
export class ProductDetailPage {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly products = inject(ProductService);
  private readonly cart = inject(CartService);
  private readonly ngZone = inject(NgZone);

  protected readonly qty = signal(1);
  protected readonly busy = signal(false);
  protected readonly msg = signal<string | null>(null);

  protected readonly product$: Observable<Product> = this.route.paramMap.pipe(
    map((p) => Number(p.get('id'))),
    switchMap((id) => this.products.get(id)),
    tap(() => {
      window.scrollTo({ top: 0, behavior: 'auto' });
      // Espera estabilidad del DOM antes de hacer scroll al bloque principal.
      this.ngZone.onStable.pipe(take(1)).subscribe(() => {
        document.getElementById('product-main')?.scrollIntoView({ behavior: 'auto', block: 'start' });
      });
    }),
  );

  /** Incrementa la cantidad seleccionada. */
  inc(): void {
    this.qty.update((q) => q + 1);
  }

  /** Decrementa cantidad sin bajar de 1. */
  dec(): void {
    this.qty.update((q) => Math.max(1, q - 1));
  }

  /** Añade el producto al carrito con la cantidad actual. */
  addToCart(p: Product): void {
    this.busy.set(true);
    this.msg.set(null);
    const q = this.qty();
    this.cart.addProduct(p, q).subscribe({
      next: () => {
        this.busy.set(false);
        this.msg.set('Agregado al carrito');
      },
      error: (e) => {
        this.busy.set(false);
        this.msg.set(e.error?.message ?? 'No se pudo agregar');
      },
    });
  }

  /** Añade al carrito y navega directamente al checkout. */
  buyNow(p: Product): void {
    this.busy.set(true);
    this.msg.set(null);
    const q = this.qty();
    this.cart.addProduct(p, q).subscribe({
      next: () => {
        this.busy.set(false);
        this.router.navigate(['/checkout']);
      },
      error: (e) => {
        this.busy.set(false);
        this.msg.set(e.error?.message ?? 'No se pudo continuar');
      },
    });
  }
}
