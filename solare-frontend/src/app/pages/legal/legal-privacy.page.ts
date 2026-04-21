import { Component } from '@angular/core';

@Component({
  standalone: true,
  template: `
    <article class="mx-auto max-w-3xl space-y-4 px-4 py-10 text-slate-700 dark:text-slate-300">
      <h1>Política de privacidad</h1>
      <p>
        Solare (proyecto demostrativo) trata datos personales conforme a la Ley 1581 de 2012 y el Decreto 1377 de 2013
        (Habeas Data en Colombia). Los datos de registro y pedidos se usan para gestionar compras, soporte y
        cumplimiento legal.
      </p>
      <p>
        Puede solicitar consulta, actualización o supresión de sus datos escribiendo a los canales oficiales (WhatsApp /
        Instagram) indicados en el sitio.
      </p>
      <p>Uso de cookies: utilizamos cookies necesarias y, con su consentimiento, preferencias. Puede retirar el consentimiento borrando cookies del navegador.</p>
    </article>
  `,
})
export class LegalPrivacyPage {}
