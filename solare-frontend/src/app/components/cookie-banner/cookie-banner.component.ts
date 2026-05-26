/**
 * @file Banner de consentimiento de cookies.
 * @description Muestra aviso hasta que el usuario acepta; persiste la decisión en `localStorage`.
 * Enlaza a la política de privacidad. Plantilla inline en el decorador.
 * @see {@link ../../app.ts}
 */

import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

const KEY = 'solare_cookies_accepted';

/** Diálogo fijo inferior con botón de aceptación única. */
@Component({
  selector: 'app-cookie-banner',
  standalone: true,
  template: `
    @if (visible()) {
      <div
        class="fixed bottom-0 left-0 right-0 z-[60] border-t border-slate-200 bg-white p-4 shadow-lg dark:border-slate-700 dark:bg-slate-800 md:left-auto md:right-4 md:bottom-4 md:max-w-md md:rounded-lg"
        role="dialog"
        aria-label="Aviso de cookies"
      >
        <p class="text-sm text-slate-700 dark:text-slate-300">
          Usamos cookies necesarias para el funcionamiento del sitio y, con tu consentimiento, para mejorar la
          experiencia. Consulta nuestra
          <a routerLink="/legal/privacidad" class="font-medium underline">política de privacidad</a>.
        </p>
        <div class="mt-3 flex gap-2">
          <button
            type="button"
            (click)="accept()"
            class="rounded-lg bg-amber-500 px-4 py-2 text-sm font-semibold text-black hover:bg-amber-400"
          >
            Aceptar
          </button>
        </div>
      </div>
    }
  `,
  imports: [RouterLink],
})
export class CookieBannerComponent {
  /** Visible solo si no existe la clave de aceptación en almacenamiento local. */
  protected readonly visible = signal(typeof localStorage !== 'undefined' && !localStorage.getItem(KEY));

  /** Registra el consentimiento y oculta el banner. */
  accept(): void {
    localStorage.setItem(KEY, '1');
    this.visible.set(false);
  }
}
