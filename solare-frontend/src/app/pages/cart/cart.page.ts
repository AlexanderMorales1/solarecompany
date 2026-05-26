/**
 * @file Página del carrito de compras.
 * @description Lista líneas, permite cambiar cantidades, eliminar ítems y ver subtotal.
 * @see {@link ../../services/cart.service.ts}
 */

import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartSummary } from '../../models/cart.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { CartService } from '../../services/cart.service';

/** Vista del carrito con recarga tras cada mutación. */
@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [RouterLink, CopCurrencyPipe],
  templateUrl: './cart.page.html',
})
export class CartPage implements OnInit {
  private readonly cartService = inject(CartService);

  protected readonly cart = signal<CartSummary | null>(null);
  protected readonly loading = signal(true);

  ngOnInit(): void {
    this.refresh();
  }

  /** Vuelve a cargar el resumen del carrito desde el servicio. */
  refresh(): void {
    this.loading.set(true);
    this.cartService.getCart().subscribe({
      next: (c) => {
        this.cart.set(c);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  /** Actualiza cantidad de una línea si es >= 1. */
  updateLine(id: number, q: number): void {
    if (q < 1) return;
    this.cartService.updateQuantity(id, q).subscribe(() => this.refresh());
  }

  /** Elimina una línea por `cartItemId`. */
  removeLine(id: number): void {
    this.cartService.removeLine(id).subscribe(() => this.refresh());
  }
}
