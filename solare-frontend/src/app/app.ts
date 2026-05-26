/**
 * @file Componente raíz de la aplicación Solare.
 * @description Define el layout global: barra de navegación, área de rutas (`router-outlet`),
 *   pie de página y banner de cookies. No contiene lógica de negocio.
 * @see {@link ./components/navbar/navbar.component.ts}
 * @see {@link ./components/footer/footer.component.ts}
 * @see {@link ./components/cookie-banner/cookie-banner.component.ts}
 * @see {@link ./app.routes.ts} Rutas hijas renderizadas en el outlet.
 */

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CookieBannerComponent } from './components/cookie-banner/cookie-banner.component';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';

/** Shell principal de la tienda: envuelve todas las páginas lazy-loaded. */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, FooterComponent, CookieBannerComponent],
  template: `
    <app-navbar />
    <main class="min-h-[60vh]">
      <router-outlet />
    </main>
    <app-footer />
    <app-cookie-banner />
  `,
})
export class App {}
