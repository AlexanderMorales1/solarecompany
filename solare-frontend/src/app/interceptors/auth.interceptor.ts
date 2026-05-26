/**
 * @file Interceptor HTTP de autenticación JWT.
 * @description Clona las peticiones salientes y añade cabecera `Authorization: Bearer` cuando
 *   existe token en `AuthService`. Registrado en `app.config.ts` vía `withInterceptors`.
 * @see {@link ../services/auth.service.ts}
 */

import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

/**
 * Interceptor funcional que adjunta el token JWT si está disponible.
 *
 * @param req Petición HTTP original.
 * @param next Siguiente manejador de la cadena.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.getToken();
  if (token) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  return next(req);
};
