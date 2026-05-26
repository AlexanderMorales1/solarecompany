/**
 * @file Página de registro de usuario.
 * @description Crea cuenta, persiste sesión y fusiona carrito local al igual que login.
 * @see {@link ../../services/auth.service.ts}
 */

import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';

/** Formulario de registro con redirección al inicio tras éxito. */
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.page.html',
})
export class RegisterPage {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly cart = inject(CartService);
  private readonly router = inject(Router);

  protected readonly error = signal<string | null>(null);
  protected readonly busy = signal(false);

  protected readonly form = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });

  /** Envía registro y navega a `/` si todo es correcto. */
  submit(): void {
    if (this.form.invalid) return;
    this.busy.set(true);
    this.error.set(null);
    const v = this.form.getRawValue();
    this.auth
      .register(v)
      .pipe(switchMap(() => this.cart.mergeLocalToServer()))
      .subscribe({
        next: () => {
          this.busy.set(false);
          this.router.navigateByUrl('/');
        },
        error: (e) => {
          this.busy.set(false);
          this.error.set(e.error?.message ?? 'No se pudo registrar');
        },
      });
  }
}
