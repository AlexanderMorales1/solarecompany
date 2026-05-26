/**
 * @file Modelos de autenticación y perfil de usuario.
 * @description Tipos alineados con las respuestas del API `/auth` del backend.
 * @see {@link ../services/auth.service.ts} Consumidor principal.
 */

/** Respuesta de login, registro u OAuth con JWT y datos básicos del usuario. */
export interface AuthResponse {
  /** Token JWT para el interceptor y almacenamiento local. */
  token: string;
  /** Esquema del token (p. ej. `Bearer`). */
  type: string;
  /** Identificador numérico del usuario. */
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  /** Roles asignados (p. ej. `ROLE_USER`, `ROLE_ADMIN`). */
  roles: string[];
}

/** Perfil devuelto por `GET /auth/me` para checkout y página de perfil. */
export interface UserProfile {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
}
