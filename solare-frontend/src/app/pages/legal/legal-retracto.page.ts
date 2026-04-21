import { Component } from '@angular/core';

@Component({
  standalone: true,
  template: `
    <article class="mx-auto max-w-3xl space-y-4 px-4 py-10 text-slate-700 dark:text-slate-300">
      <h1>Derecho de retracto</h1>
      <p>
        En ventas a distancia en Colombia, el consumidor puede ejercer el derecho de retracto dentro de los cinco (5)
        días hábiles siguientes a la entrega del bien, salvo excepciones legales (p. ej. bienes personalizados, ciertos
        productos de higiene, entre otros).
      </p>
      <p>
        Para ejercerlo, el cliente debe notificar por los canales oficiales y devolver el producto en condiciones
        adecuadas según política de devoluciones.
      </p>
    </article>
  `,
})
export class LegalRetractoPage {}
