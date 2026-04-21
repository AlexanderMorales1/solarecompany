import { Component, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent {
  protected readonly auth = inject(AuthService);
  protected readonly theme = inject(ThemeService);
  private readonly cartService = inject(CartService);
  private readonly router = inject(Router);

  protected readonly mobileOpen = signal(false);
  protected cartCount = signal(0);

  constructor() {
    this.refreshCartCount();
    this.router.events.pipe(filter((e) => e instanceof NavigationEnd)).subscribe(() => {
      this.refreshCartCount();
    });
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
}
