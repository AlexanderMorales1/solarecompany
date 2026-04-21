import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CartSummary } from '../../models/cart.model';
import { PaymentMethod } from '../../models/order.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [FormsModule, CopCurrencyPipe],
  templateUrl: './checkout.page.html',
})
export class CheckoutPage implements OnInit {
  private readonly cartService = inject(CartService);
  private readonly orders = inject(OrderService);
  private readonly router = inject(Router);

  protected readonly cart = signal<CartSummary | null>(null);
  protected method: PaymentMethod = 'BOLD';
  protected readonly busy = signal(false);
  protected readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.cartService.getCart().subscribe((c) => this.cart.set(c));
  }

  submit(): void {
    const c = this.cart();
    if (!c || c.items.length === 0) {
      this.error.set('El carrito está vacío');
      return;
    }
    this.busy.set(true);
    this.error.set(null);
    this.orders.checkout(this.method).subscribe({
      next: () => {
        this.busy.set(false);
        this.router.navigate(['/perfil'], { queryParams: { ok: '1' } });
      },
      error: (e) => {
        this.busy.set(false);
        this.error.set(e.error?.message ?? 'No se pudo completar el pago simulado');
      },
    });
  }
}
