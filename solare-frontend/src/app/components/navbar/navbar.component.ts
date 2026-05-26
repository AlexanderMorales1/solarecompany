/**
 * @file Barra de navegación principal.
 * @description Enlaces de tienda, búsqueda, carrito, tema oscuro, sesión y menú móvil.
 * Sincroniza el contador del carrito con navegación y cambios en `CartService`.
 * @see {@link ../../services/auth.service.ts}
 * @see {@link ../../services/cart.service.ts}
 * @see {@link ../../services/theme.service.ts}
 */

import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { filter, merge, startWith } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { ThemeService } from '../../services/theme.service';

/** Navbar standalone con búsqueda que navega a `/tienda` con query params. */
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, ReactiveFormsModule],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent {
  protected readonly auth = inject(AuthService);
  protected readonly theme = inject(ThemeService);
  private readonly cartService = inject(CartService);
  private readonly router = inject(Router);

  protected readonly mobileOpen = signal(false);
  protected cartCount = signal(0);
  protected readonly searchCtrl = new FormControl('', { nonNullable: true });

  constructor() {
    // Recalcula el badge del carrito al cambiar de ruta o al mutar el carrito.
    merge(
      this.router.events.pipe(filter((e) => e instanceof NavigationEnd)),
      this.cartService.cartChanged,
    )
      .pipe(startWith(null))
      .subscribe(() => this.refreshCartCount());
  }

  /** Abre o cierra el menú de navegación en vista móvil. */
  toggleMobile(): void {
    this.mobileOpen.update((v) => !v);
  }

  /** Delega el cambio de tema a `ThemeService`. */
  toggleTheme(): void {
    this.theme.toggle();
  }

  /** Cierra sesión y actualiza el contador del carrito (puede pasar a modo local). */
  logout(): void {
    this.auth.logout();
    this.refreshCartCount();
  }

  /** Suma cantidades de todas las líneas del carrito para el badge. */
  refreshCartCount(): void {
    this.cartService.getCart().subscribe({
      next: (c) => {
        const n = c.items.reduce((s, i) => s + i.quantity, 0);
        this.cartCount.set(n);
      },
      error: () => this.cartCount.set(0),
    });
  }

  /**
   * Navega al catálogo con el término de búsqueda.
   * Si ya está en `/tienda`, fusiona query params; si no, reemplaza y reinicia página.
   */
  search(): void {
    const query = this.searchCtrl.value.trim();
    const onStore = this.router.url.startsWith('/tienda');
    void this.router.navigate(['/tienda'], {
      queryParams: {
        q: query || null,
        page: 0,
      },
      queryParamsHandling: onStore ? 'merge' : undefined,
    });
    this.mobileOpen.set(false);
  }
}
