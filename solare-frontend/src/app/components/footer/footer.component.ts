/**
 * @file Pie de página global.
 * @description Muestra enlaces legales, redes y año actual. Plantilla en `footer.component.html`.
 * @see {@link ../../app.ts} Incluido en el layout raíz.
 */

import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

/** Footer estático con navegación a páginas legales. */
@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './footer.component.html',
})
export class FooterComponent {
  /** Año actual para el copyright. */
  protected readonly year = new Date().getFullYear();
}
