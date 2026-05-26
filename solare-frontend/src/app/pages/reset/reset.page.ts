/**
 * @file Página de restablecimiento de contraseña.
 * @description Formulario con token (desde query `token`) y nueva contraseña.
 * @see {@link ../../services/auth.service.ts}
 */

import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/** Restablece contraseña y redirige a login tras éxito. */
@Component({
  selector: 'app-reset',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './reset.page.html',
})
export class ResetPage implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  protected readonly error = signal<string | null>(null);
  protected readonly busy = signal(false);

  protected readonly form = this.fb.nonNullable.group({
    token: ['', Validators.required],
    newPassword: ['', [Validators.required, Validators.minLength(8)]],
  });

  ngOnInit(): void {
    const t = this.route.snapshot.queryParamMap.get('token');
    if (t) this.form.patchValue({ token: t });
  }

  /** Envía token y nueva contraseña al API. */
  submit(): void {
    if (this.form.invalid) return;
    this.busy.set(true);
    this.error.set(null);
    const v = this.form.getRawValue();
    this.auth.resetPassword(v.token, v.newPassword).subscribe({
      next: () => {
        this.busy.set(false);
        this.router.navigate(['/login']);
      },
      error: (e) => {
        this.busy.set(false);
        this.error.set(e.error?.message ?? 'Token inválido');
      },
    });
  }
}
