/**
 * @file Página de recuperación de contraseña.
 * @description Solicita al backend el envío de instrucciones por email (flujo simulado/demo).
 * @see {@link ../../services/auth.service.ts}
 */

import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/** Formulario de email para `forgot-password`. */
@Component({
  selector: 'app-forgot',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './forgot.page.html',
})
export class ForgotPage {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);

  protected readonly done = signal(false);
  protected readonly error = signal<string | null>(null);

  protected readonly form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
  });

  /** Envía la solicitud y muestra estado de confirmación sin revelar si el email existe. */
  submit(): void {
    if (this.form.invalid) return;
    this.error.set(null);
    this.auth.forgotPassword(this.form.controls.email.value).subscribe({
      next: () => this.done.set(true),
      error: (e) => this.error.set(e.error?.message ?? 'Error al solicitar recuperación'),
    });
  }
}
