/**
 * @file Pipe de formato de moneda colombiana (COP).
 * @description Transforma números a cadena con `Intl.NumberFormat` locale `es-CO`.
 * @see Usado en plantillas de catálogo, carrito, checkout, perfil y admin.
 */

import { Pipe, PipeTransform } from '@angular/core';

/** Pipe standalone `copCurrency` para mostrar precios sin decimales. */
@Pipe({ name: 'copCurrency', standalone: true })
export class CopCurrencyPipe implements PipeTransform {
  /**
   * Formatea un valor numérico como pesos colombianos.
   *
   * @param value Monto en COP; valores nulos o NaN devuelven cadena vacía.
   * @returns Cadena formateada (p. ej. `$ 270.000`).
   */
  transform(value: number | null | undefined): string {
    if (value == null || Number.isNaN(value)) return '';
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      maximumFractionDigits: 0,
    }).format(value);
  }
}
