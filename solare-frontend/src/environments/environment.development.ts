/**
 * @file Configuración de entorno para desarrollo local.
 * @description Reemplaza a `environment.ts` en builds de desarrollo (`ng serve`).
 *   Apunta al backend en localhost para pruebas integradas.
 * @see {@link ./environment.ts} Contrato equivalente en producción.
 */

/** Variables de entorno del modo desarrollo. */
export const environment = {
  /** Siempre `false` en este archivo de desarrollo. */
  production: false,
  /** API local del backend Spring Boot. */
  apiUrl: 'http://localhost:8080/api',
};
