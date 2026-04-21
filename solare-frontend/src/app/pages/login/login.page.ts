import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.page.html',
})
export class LoginPage {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly cart = inject(CartService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  protected readonly error = signal<string | null>(null);
  protected readonly busy = signal(false);

  protected readonly form = this.fb.nonNullable.group({
    email: [this.auth.getRememberedEmail() ?? '', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    remember: [!!this.auth.getRememberedEmail()],
  });

  google(): void {
    this.auth.getGoogleLoginUrl().subscribe({
      next: (r) => {
        window.location.href = r.url;
      },
      error: () => this.error.set('No se pudo iniciar Google OAuth. Revisa el backend.'),
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.busy.set(true);
    this.error.set(null);
    const v = this.form.getRawValue();
    if (v.remember) this.auth.rememberEmail(v.email);
    else this.auth.forgetRememberedEmail();

    this.auth
      .login({ email: v.email, password: v.password })
      .pipe(switchMap(() => this.cart.mergeLocalToServer()))
      .subscribe({
        next: () => {
          this.busy.set(false);
          const next = this.route.snapshot.queryParamMap.get('next') || '/';
          this.router.navigateByUrl(next);
        },
        error: (e) => {
          this.busy.set(false);
          this.error.set(e.error?.message ?? 'Credenciales inválidas');
        },
      });
  }
}
