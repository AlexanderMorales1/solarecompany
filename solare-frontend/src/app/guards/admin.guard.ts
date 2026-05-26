/**
 * @file Guard de rutas reservadas a administradores.
 * @description Comprueba sesión y rol `ROLE_ADMIN` decodificado del JWT; si falla, redirige al inicio.
 * @see {@link ../services/auth.service.ts} Señal computada `isAdmin`.
 * @see {@link ../app.routes.ts} Ruta `/admin`.
 */

import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Permite acceso al panel admin solo para usuarios logueados con rol de administrador.
 */
export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (auth.isLoggedIn() && auth.isAdmin()) return true;
  return router.createUrlTree(['/']);
};
