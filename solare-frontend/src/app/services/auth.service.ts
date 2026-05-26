/**
 * @file Servicio de autenticación y sesión del usuario.
 * @description Gestiona JWT en `localStorage`, login/registro/OAuth, recuperación de contraseña
 *   y señales reactivas de estado (`isLoggedIn`, `isAdmin`). Usado por guards e interceptor.
 * @see {@link ../models/auth.model.ts}
 * @see {@link ../../environments/environment.ts}
 */

import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthResponse, UserProfile } from '../models/auth.model';

const TOKEN_KEY = 'solare_token';
const REMEMBER_EMAIL_KEY = 'solare_remember_email';

/** Servicio raíz (`providedIn: 'root'`) para autenticación y persistencia de sesión. */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private readonly tokenSig = signal<string | null>(localStorage.getItem(TOKEN_KEY));

  /** Token JWT actual (solo lectura). */
  readonly token = this.tokenSig.asReadonly();
  /** `true` si hay token almacenado. */
  readonly isLoggedIn = computed(() => !!this.tokenSig());
  /** `true` si el payload del JWT incluye `ROLE_ADMIN`. */
  readonly isAdmin = computed(() => {
    const t = this.tokenSig();
    if (!t) return false;
    try {
      // Decodificación manual del payload JWT (segmento central en base64).
      const payload = JSON.parse(atob(t.split('.')[1]));
      const roles = String(payload.roles ?? '');
      return roles.includes('ROLE_ADMIN');
    } catch {
      return false;
    }
  });

  /** Obtiene el token actual para el interceptor HTTP. */
  getToken(): string | null {
    return this.tokenSig();
  }

  /**
   * Inicia sesión con email y contraseña.
   * @param body Credenciales del usuario.
   */
  login(body: { email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, body).pipe(
      tap((res) => this.persistToken(res.token)),
    );
  }

  /**
   * Registra un nuevo usuario y persiste el token devuelto.
   * @param body Datos de registro.
   */
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

  /** Guarda el token recibido tras redirección OAuth (callback). */
  setTokenFromOAuth(token: string): void {
    this.persistToken(token);
  }

  /** Escribe el token en `localStorage` y actualiza la señal. */
  persistToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    this.tokenSig.set(token);
  }

  /** Cierra sesión, borra el token y navega al inicio. */
  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    this.tokenSig.set(null);
    this.router.navigate(['/']);
  }

  /** Obtiene el perfil del usuario autenticado. */
  me(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${environment.apiUrl}/auth/me`);
  }

  /** Solicita enlace/correo de recuperación de contraseña. */
  forgotPassword(email: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${environment.apiUrl}/auth/forgot-password`, { email });
  }

  /** Restablece la contraseña con el token del correo. */
  resetPassword(token: string, newPassword: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${environment.apiUrl}/auth/reset-password`, {
      token,
      newPassword,
    });
  }

  /** URL de redirección para inicio de sesión con Google OAuth2. */
  getGoogleLoginUrl(): Observable<{ url: string }> {
    return this.http.get<{ url: string }>(`${environment.apiUrl}/auth/oauth2/google`);
  }

  /** Guarda el email para prellenar el formulario de login. */
  rememberEmail(email: string): void {
    localStorage.setItem(REMEMBER_EMAIL_KEY, email);
  }

  /** Elimina el email recordado. */
  forgetRememberedEmail(): void {
    localStorage.removeItem(REMEMBER_EMAIL_KEY);
  }

  /** Devuelve el email guardado o `null`. */
  getRememberedEmail(): string | null {
    return localStorage.getItem(REMEMBER_EMAIL_KEY);
  }
}
