import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CartSummary } from '../../models/cart.model';
import { PaymentMethod } from '../../models/order.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [ReactiveFormsModule, CopCurrencyPipe],
  templateUrl: './checkout.page.html',
})
export class CheckoutPage implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly cartService = inject(CartService);
  private readonly orders = inject(OrderService);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);

  protected readonly cart = signal<CartSummary | null>(null);
  protected readonly busy = signal(false);
  protected readonly error = signal<string | null>(null);

  private readonly phonePattern = /^[+]?[0-9\s\-]{10,20}$/;

  protected readonly form = this.fb.nonNullable.group({
    fullName: ['', [Validators.required, Validators.maxLength(200)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
    phoneNumber: ['', [Validators.required, Validators.pattern(this.phonePattern)]],
    country: ['Colombia', [Validators.required, Validators.maxLength(100)]],
    department: ['', [Validators.required, Validators.maxLength(100)]],
    city: ['', [Validators.required, Validators.maxLength(100)]],
    neighborhood: ['', [Validators.required, Validators.maxLength(120)]],
    address: ['', [Validators.required, Validators.maxLength(500)]],
    paymentMethod: this.fb.nonNullable.control<PaymentMethod>('BOLD', [Validators.required]),
  });

  ngOnInit(): void {
    this.cartService.getCart().subscribe((c) => this.cart.set(c));
    this.auth.me().subscribe({
      next: (p) => {
        const full = `${p.firstName ?? ''} ${p.lastName ?? ''}`.trim();
        this.form.patchValue({
          email: p.email ?? '',
          ...(full ? { fullName: full } : {}),
        });
      },
      error: () => {},
    });
  }

  submit(): void {
    const c = this.cart();
    if (!c || c.items.length === 0) {
      this.error.set('El carrito está vacío');
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.error.set('Revisa los campos marcados.');
      return;
    }
    this.busy.set(true);
    this.error.set(null);
    const v = this.form.getRawValue();
    this.orders
      .checkout({
        paymentMethod: v.paymentMethod,
        fullName: v.fullName.trim(),
        email: v.email.trim(),
        phoneNumber: v.phoneNumber.trim(),
        country: v.country.trim(),
        department: v.department.trim(),
        city: v.city.trim(),
        neighborhood: v.neighborhood.trim(),
        address: v.address.trim(),
      })
      .subscribe({
        next: () => {
          this.busy.set(false);
          this.router.navigate(['/perfil'], { queryParams: { ok: '1' } });
        },
        error: (e: HttpErrorResponse) => {
          this.busy.set(false);
          const body = e.error as { message?: string; fieldErrors?: Record<string, string> } | undefined;
          if (body?.fieldErrors) {
            for (const [key, msg] of Object.entries(body.fieldErrors)) {
              const ctrl = this.form.get(key);
              if (ctrl) {
                ctrl.setErrors({ server: msg });
                ctrl.markAsTouched();
              }
            }
          }
          this.error.set(body?.message ?? 'No se pudo completar el pago simulado');
        },
      });
  }

  fieldInvalid(name: string): boolean {
    const c = this.form.get(name);
    return !!c && c.invalid && (c.dirty || c.touched);
  }
}
