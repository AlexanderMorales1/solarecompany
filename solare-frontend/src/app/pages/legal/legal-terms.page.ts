/**
 * @file Página legal: términos y condiciones.
 * @description Texto informativo sobre precios, medios de pago simulados y propiedad intelectual.
 */

import { Component } from '@angular/core';

/** Términos de uso de la tienda demostrativa. */
@Component({
  standalone: true,
  template: `
    <article class="mx-auto max-w-3xl space-y-4 px-4 py-10 text-slate-700 dark:text-slate-300">
      <h1>Términos y condiciones</h1>
      <p>
        Al usar Solare usted acepta estos términos. Los precios se expresan en COP. La disponibilidad depende del stock.
        Las descripciones y fotos son orientativas.
      </p>
      <p>
        Medios de pago simulados (Sistecrédito, Addi, Bold) representan integraciones futuras; en este entorno la
        transacción no genera cobro real.
      </p>
      <p>Propiedad intelectual: marcas mencionadas pertenecen a sus titulares. Este sitio es una demostración técnica.</p>
    </article>
  `,
})
export class LegalTermsPage {}
