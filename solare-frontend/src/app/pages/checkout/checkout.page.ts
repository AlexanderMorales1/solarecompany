/**
 * @file Página de checkout (pago y envío).
 * @description Formulario de datos de entrega, selección de medio de pago simulado,
 *   sandbox Wompi y envío del pedido al API. Requiere autenticación (`authGuard`).
 * @see {@link ../../services/order.service.ts}
 * @see {@link ../../services/cart.service.ts}
 * @see {@link ../../services/auth.service.ts}
 */

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

/** Checkout con prellenado desde perfil y modal sandbox para Wompi. */
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
  protected readonly wompiModalOpen = signal(false);
  protected readonly wompiStatus = signal<'idle' | 'loading' | 'success' | 'error'>('idle');
  protected readonly wompiMessage = signal<string | null>(null);

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

  /**
   * Valida formulario y carrito; Wompi abre sandbox antes de llamar al API.
   */
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
    this.wompiMessage.set(null);
    const paymentMethod = this.form.getRawValue().paymentMethod;
    if (paymentMethod === 'WOMPI') {
      this.busy.set(false);
      this.openWompiSandbox();
      return;
    }
    this.performCheckout();
  }

  /** POST `/orders/checkout` con datos del formulario. */
  private performCheckout(): void {
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
          this.wompiModalOpen.set(false);
          this.router.navigate(['/perfil'], { queryParams: { ok: '1' } });
        },
        error: (e: HttpErrorResponse) => {
          this.busy.set(false);
          const body = e.error as { message?: string; fieldErrors?: Record<string, string> } | undefined;
          // Mapea errores de validación del servidor a controles del formulario.
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

  /** Abre el modal de pago simulado Wompi. */
  openWompiSandbox(): void {
    this.wompiModalOpen.set(true);
    this.wompiStatus.set('idle');
  }

  /** Cierra el modal salvo mientras está en estado `loading`. */
  closeWompiSandbox(): void {
    if (this.wompiStatus() === 'loading') return;
    this.wompiModalOpen.set(false);
    this.wompiStatus.set('idle');
    this.wompiMessage.set(null);
  }

  /**
   * Simula latencia de pasarela; ~80 % éxito aleatorio antes de ejecutar checkout real.
   */
  confirmWompiSandboxPayment(): void {
    this.wompiStatus.set('loading');
    this.wompiMessage.set('Procesando pago en sandbox...');
    setTimeout(() => {
      const success = Math.random() >= 0.2;
      if (success) {
        this.wompiStatus.set('success');
        this.wompiMessage.set('Pago simulado aprobado por Wompi Sandbox');
        this.performCheckout();
        return;
      }
      this.wompiStatus.set('error');
      this.wompiMessage.set('Pago simulado rechazado. Intenta de nuevo.');
    }, 1800);
  }

  /** Indica si un campo debe mostrarse como inválido en la plantilla. */
  fieldInvalid(name: string): boolean {
    const c = this.form.get(name);
    return !!c && c.invalid && (c.dirty || c.touched);
  }
}
