/**
 * @file Configuración de proveedores de la aplicación Angular.
 * @description Registra detección de zona, animaciones, enrutador con scroll al inicio,
 *   cliente HTTP con el interceptor de autenticación y escucha global de errores.
 * @see {@link ./app.routes.ts} Definición de rutas.
 * @see {@link ./interceptors/auth.interceptor.ts} Inyección del token JWT en peticiones.
 */

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter, withInMemoryScrolling } from '@angular/router';
import { routes } from './app.routes';
import { authInterceptor } from './interceptors/auth.interceptor';

/** Proveedores exportados para `bootstrapApplication` en `main.ts`. */
export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideAnimations(),
    provideRouter(
      routes,
      withInMemoryScrolling({
        scrollPositionRestoration: 'top',
        anchorScrolling: 'enabled',
      }),
    ),
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
};
