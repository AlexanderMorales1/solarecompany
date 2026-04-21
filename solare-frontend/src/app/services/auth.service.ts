import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthResponse, UserProfile } from '../models/auth.model';

const TOKEN_KEY = 'solare_token';
const REMEMBER_EMAIL_KEY = 'solare_remember_email';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private readonly tokenSig = signal<string | null>(localStorage.getItem(TOKEN_KEY));

  readonly token = this.tokenSig.asReadonly();
  readonly isLoggedIn = computed(() => !!this.tokenSig());
  readonly isAdmin = computed(() => {
    const t = this.tokenSig();
    if (!t) return false;
    try {
      const payload = JSON.parse(atob(t.split('.')[1]));
      const roles = String(payload.roles ?? '');
      return roles.includes('ROLE_ADMIN');
    } catch {
      return false;
    }
  });

  getToken(): string | null {
    return this.tokenSig();
  }

  login(body: { email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, body).pipe(
      tap((res) => this.persistToken(res.token)),
    );
  }

  register(body: {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
  }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, body).pipe(
      tap((res) => this.persistToken(res.token)),
    );
  }

  setTokenFromOAuth(token: string): void {
    this.persistToken(token);
  }

  persistToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    this.tokenSig.set(token);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    this.tokenSig.set(null);
    this.router.navigate(['/']);
  }

  me(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${environment.apiUrl}/auth/me`);
  }

  forgotPassword(email: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${environment.apiUrl}/auth/forgot-password`, { email });
  }

  resetPassword(token: string, newPassword: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${environment.apiUrl}/auth/reset-password`, {
      token,
      newPassword,
    });
  }

  getGoogleLoginUrl(): Observable<{ url: string }> {
    return this.http.get<{ url: string }>(`${environment.apiUrl}/auth/oauth2/google`);
  }

  rememberEmail(email: string): void {
    localStorage.setItem(REMEMBER_EMAIL_KEY, email);
  }

  forgetRememberedEmail(): void {
    localStorage.removeItem(REMEMBER_EMAIL_KEY);
  }

  getRememberedEmail(): string | null {
    return localStorage.getItem(REMEMBER_EMAIL_KEY);
  }
}
