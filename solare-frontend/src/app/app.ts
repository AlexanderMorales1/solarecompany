import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CookieBannerComponent } from './components/cookie-banner/cookie-banner.component';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, FooterComponent, CookieBannerComponent],
  template: `
    <app-navbar />
    <main class="min-h-[60vh]">
      <router-outlet />
    </main>
    <app-footer />
    <app-cookie-banner />
  `,
})
export class App {}
