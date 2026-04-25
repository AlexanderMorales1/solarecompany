import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { filter, merge, startWith } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { ThemeService } from '../../services/theme.service';

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
    merge(
      this.router.events.pipe(filter((e) => e instanceof NavigationEnd)),
      this.cartService.cartChanged,
    )
      .pipe(startWith(null))
      .subscribe(() => this.refreshCartCount());
  }

  toggleMobile(): void {
    this.mobileOpen.update((v) => !v);
  }

  toggleTheme(): void {
    this.theme.toggle();
  }

  logout(): void {
    this.auth.logout();
    this.refreshCartCount();
  }

  refreshCartCount(): void {
    this.cartService.getCart().subscribe({
      next: (c) => {
        const n = c.items.reduce((s, i) => s + i.quantity, 0);
        this.cartCount.set(n);
      },
      error: () => this.cartCount.set(0),
    });
  }

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
