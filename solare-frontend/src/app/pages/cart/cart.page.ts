import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartSummary } from '../../models/cart.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { CartService } from '../../services/cart.service';

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

  updateLine(id: number, q: number): void {
    if (q < 1) return;
    this.cartService.updateQuantity(id, q).subscribe(() => this.refresh());
  }

  removeLine(id: number): void {
    this.cartService.removeLine(id).subscribe(() => this.refresh());
  }
}
