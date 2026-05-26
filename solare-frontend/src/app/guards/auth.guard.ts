/**
 * @file Guard de rutas que exigen usuario autenticado.
 * @description Redirige a `/login` con query `next` si no hay token válido en `AuthService`.
 * @see {@link ../services/auth.service.ts}
 * @see {@link ../app.routes.ts} Rutas `checkout`, `perfil` y `admin` (junto con adminGuard).
 */

import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Permite la navegación solo si hay sesión activa; en caso contrario devuelve URL tree a login.
 *
 * @param _route Ruta solicitada (no usada).
 * @param state Estado del router con la URL destino para `next`.
 */
export const authGuard: CanActivateFn = (_route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (auth.isLoggedIn()) return true;
  return router.createUrlTree(['/login'], { queryParams: { next: state.url } });
};
