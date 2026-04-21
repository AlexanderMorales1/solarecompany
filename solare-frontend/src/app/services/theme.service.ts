import { Injectable, signal } from '@angular/core';

const KEY = 'solare_theme';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly darkSig = signal(this.readInitial());

  readonly darkMode = this.darkSig.asReadonly();

  constructor() {
    this.apply(this.darkSig());
  }

  toggle(): void {
    const next = !this.darkSig();
    this.darkSig.set(next);
    localStorage.setItem(KEY, next ? 'dark' : 'light');
    this.apply(next);
  }

  private readInitial(): boolean {
    const v = localStorage.getItem(KEY);
    if (v === 'dark') return true;
    if (v === 'light') return false;
    return window.matchMedia?.('(prefers-color-scheme: dark)').matches ?? false;
  }

  private apply(dark: boolean): void {
    const root = document.documentElement;
    if (dark) root.classList.add('dark');
    else root.classList.remove('dark');
  }
}
