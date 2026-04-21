import { Component } from '@angular/core';

@Component({
  standalone: true,
  template: `
    <article class="mx-auto max-w-3xl space-y-4 px-4 py-10 text-slate-700 dark:text-slate-300">
      <h1>Información comercial y tributaria</h1>
      <p>
        De acuerdo con la normativa colombiana aplicable al comercio electrónico, el comerciante debe informar
        identificación tributaria (NIT), responsabilidad de IVA y Régimen Tributario Simple (RTS) o común, según
        corresponda.
      </p>
      <p>
        <strong>Nota demostrativa:</strong> complete aquí NIT, razón social, dirección y teléfono del comercio real
        antes de producción. Las facturas electrónicas deben emitirse según Resolución DIAN vigente.
      </p>
    </article>
  `,
})
export class LegalRntPage {}
