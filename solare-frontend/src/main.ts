/**
 * @file Punto de entrada de la aplicación Angular.
 * @description Arranca la app standalone con `bootstrapApplication`, usando la configuración
 *   de `app/app.config.ts` y el componente raíz `App`.
 * @see {@link ./app/app.config.ts} Proveedores HTTP, router, animaciones e interceptores.
 * @see {@link ./app/app.ts} Shell visual (navbar, outlet, footer, cookies).
 */

import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
