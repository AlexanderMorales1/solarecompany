/**
 * @file Configuración de entorno por defecto (build de producción).
 * @description Expone la URL base del API y el flag `production`. Sustituido en desarrollo
 *   por `environment.development.ts` según la configuración de `angular.json`.
 * @see {@link ../app/services/*.service.ts} Servicios que consumen `environment.apiUrl`.
 */

/** Variables de entorno usadas en tiempo de compilación/ejecución del frontend. */
export const environment = {
  /** Indica si el build es de producción. */
  production: false,
  /** Prefijo de la API REST del backend Solare. */
  apiUrl: 'http://localhost:8080/api',
};
