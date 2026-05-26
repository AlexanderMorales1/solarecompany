/**
 * @file Tabla de rutas de la SPA Solare.
 * @description Define rutas públicas, legales, autenticación y áreas protegidas con
 *   `authGuard` y `adminGuard`. Las páginas se cargan con lazy loading (`loadComponent`).
 * @see {@link ./guards/auth.guard.ts} Rutas que exigen sesión.
 * @see {@link ./guards/admin.guard.ts} Panel de administración.
 */

import { Routes } from '@angular/router';
import { adminGuard } from './guards/admin.guard';
import { authGuard } from './guards/auth.guard';

/** Rutas principales de la tienda y contenido legal. */
export const routes: Routes = [
  { path: '', loadComponent: () => import('./pages/home/home.page').then((m) => m.HomePage) },
  { path: 'tienda', loadComponent: () => import('./pages/catalog/catalog.page').then((m) => m.CatalogPage) },
  {
    path: 'producto/:id',
    loadComponent: () => import('./pages/product-detail/product-detail.page').then((m) => m.ProductDetailPage),
  },
  { path: 'carrito', loadComponent: () => import('./pages/cart/cart.page').then((m) => m.CartPage) },
  {
    path: 'checkout',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/checkout/checkout.page').then((m) => m.CheckoutPage),
  },
  { path: 'login', loadComponent: () => import('./pages/login/login.page').then((m) => m.LoginPage) },
  { path: 'registro', loadComponent: () => import('./pages/register/register.page').then((m) => m.RegisterPage) },
  { path: 'recuperar', loadComponent: () => import('./pages/forgot/forgot.page').then((m) => m.ForgotPage) },
  { path: 'restablecer', loadComponent: () => import('./pages/reset/reset.page').then((m) => m.ResetPage) },
  {
    path: 'perfil',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/profile/profile.page').then((m) => m.ProfilePage),
  },
  {
    path: 'auth/callback',
    loadComponent: () => import('./pages/auth-callback/auth-callback.page').then((m) => m.AuthCallbackPage),
  },
  {
    path: 'admin',
    canActivate: [authGuard, adminGuard],
    loadComponent: () => import('./pages/admin/admin.page').then((m) => m.AdminPage),
  },
  {
    path: 'legal/privacidad',
    loadComponent: () => import('./pages/legal/legal-privacy.page').then((m) => m.LegalPrivacyPage),
  },
  {
    path: 'legal/terminos',
    loadComponent: () => import('./pages/legal/legal-terms.page').then((m) => m.LegalTermsPage),
  },
  { path: 'legal/rnt', loadComponent: () => import('./pages/legal/legal-rnt.page').then((m) => m.LegalRntPage) },
  {
    path: 'legal/derecho-retracto',
    loadComponent: () => import('./pages/legal/legal-retracto.page').then((m) => m.LegalRetractoPage),
  },
  { path: '**', redirectTo: '' },
];
