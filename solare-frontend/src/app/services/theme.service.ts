/**
 * @file Servicio de tema claro/oscuro.
 * @description Persiste preferencia en `localStorage`, respeta `prefers-color-scheme` por defecto
 *   y aplica la clase `dark` en `document.documentElement` para Tailwind.
 * @see {@link ../components/navbar/navbar.component.ts} Botón de alternancia.
 */

import { Injectable, signal } from '@angular/core';

const KEY = 'solare_theme';

/** Gestión del modo oscuro global de la UI. */
@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly darkSig = signal(this.readInitial());

  /** Señal de solo lectura: `true` si el tema oscuro está activo. */
  readonly darkMode = this.darkSig.asReadonly();

  constructor() {
    this.apply(this.darkSig());
  }

  /** Alterna entre tema claro y oscuro y persiste la elección. */
  toggle(): void {
    const next = !this.darkSig();
    this.darkSig.set(next);
    localStorage.setItem(KEY, next ? 'dark' : 'light');
    this.apply(next);
  }

  /** Lee preferencia guardada o la del sistema operativo. */
  private readInitial(): boolean {
    const v = localStorage.getItem(KEY);
    if (v === 'dark') return true;
    if (v === 'light') return false;
    return window.matchMedia?.('(prefers-color-scheme: dark)').matches ?? false;
  }

  /** Añade o quita la clase `dark` en la raíz del documento. */
  private apply(dark: boolean): void {
    const root = document.documentElement;
    if (dark) root.classList.add('dark');
    else root.classList.remove('dark');
  }
}
