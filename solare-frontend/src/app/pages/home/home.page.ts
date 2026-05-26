/**
 * @file Página de inicio (landing).
 * @description Carrusel de banners activos, productos destacados por marca (Ferrati/Ray-Ban)
 *   y sección de novedades. Gestiona auto-slide del carrusel cada 5 segundos.
 * @see {@link ../../services/home-banner.service.ts}
 * @see {@link ../../services/product.service.ts}
 */

import { AsyncPipe } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Observable, forkJoin, map } from 'rxjs';
import { HomeBanner } from '../../models/banner.model';
import { Product } from '../../models/product.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { HomeBannerService } from '../../services/home-banner.service';
import { ProductService } from '../../services/product.service';

/** Home con carrusel, destacados y productos recientes. */
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, AsyncPipe, CopCurrencyPipe],
  templateUrl: './home.page.html',
})
export class HomePage implements OnInit, OnDestroy {
  private readonly products = inject(ProductService);
  private readonly homeBannersApi = inject(HomeBannerService);
  private carouselTimer: ReturnType<typeof setInterval> | null = null;

  protected readonly data$: Observable<{ ferrati: Product[]; rayban: Product[] }> = forkJoin({
    ferrati: this.products.featuredByBrand('FERRATI'),
    rayban: this.products.featuredByBrand('RAYBAN'),
  }).pipe(map((d) => d));
  protected readonly recent$: Observable<Product[]> = this.products.recent(8).pipe(map((r) => r.content));

  protected readonly fallbackBanner =
    '/image/Photoroom-20250724_092901_4.png';
  protected readonly banners = signal<HomeBanner[]>([]);
  protected readonly activeBannerIndex = signal(0);
  protected readonly bannersLoading = signal(true);
  protected readonly bannerError = signal<string | null>(null);

  ngOnInit(): void {
    this.homeBannersApi.listActive().subscribe({
      next: (items) => {
        this.banners.set(items);
        this.activeBannerIndex.set(0);
        this.bannersLoading.set(false);
        this.bannerError.set(null);
        this.startAutoSlide();
      },
      error: () => {
        this.banners.set([]);
        this.activeBannerIndex.set(0);
        this.bannersLoading.set(false);
        this.bannerError.set('No se pudieron cargar los banners publicitarios.');
      },
    });
  }

  ngOnDestroy(): void {
    this.stopAutoSlide();
  }

  /** Avanza al siguiente banner y reinicia el temporizador automático. */
  protected nextBanner(): void {
    const items = this.banners();
    if (items.length <= 1) return;
    this.activeBannerIndex.set((this.activeBannerIndex() + 1) % items.length);
    this.startAutoSlide();
  }

  /** Retrocede un banner en el carrusel circular. */
  protected previousBanner(): void {
    const items = this.banners();
    if (items.length <= 1) return;
    this.activeBannerIndex.set((this.activeBannerIndex() - 1 + items.length) % items.length);
    this.startAutoSlide();
  }

  /** Selecciona un banner por índice (puntos del carrusel). */
  protected setBanner(index: number): void {
    if (index < 0 || index >= this.banners().length) return;
    this.activeBannerIndex.set(index);
    this.startAutoSlide();
  }

  /** URL de imagen del banner activo o imagen de respaldo. */
  protected currentBannerImage(): string {
    const items = this.banners();
    if (items.length === 0) return this.fallbackBanner;
    return items[this.activeBannerIndex()]?.imageUrl || this.fallbackBanner;
  }

  protected currentBannerTitle(): string {
    const items = this.banners();
    return items[this.activeBannerIndex()]?.title || 'Rendimiento y estilo Ferrati';
  }

  protected currentBannerSubtitle(): string {
    const items = this.banners();
    return (
      items[this.activeBannerIndex()]?.subtitle ||
      'Lentes deportivos usados por atletas que exigen protección UV y diseño aerodinámico.'
    );
  }

  /** Inicia rotación automática cada 5 s si hay más de un banner. */
  private startAutoSlide(): void {
    this.stopAutoSlide();
    if (this.banners().length <= 1) return;
    this.carouselTimer = setInterval(() => this.nextBanner(), 5000);
  }

  private stopAutoSlide(): void {
    if (!this.carouselTimer) return;
    clearInterval(this.carouselTimer);
    this.carouselTimer = null;
  }

  /**
   * Indica si el producto se creó en los últimos 7 días (etiqueta "Nuevo").
   * @param p Producto con `createdAt` opcional ISO.
   */
  protected isNewProduct(p: Product): boolean {
    if (!p.createdAt) return false;
    const created = new Date(p.createdAt).getTime();
    if (Number.isNaN(created)) return false;
    const sevenDays = 7 * 24 * 60 * 60 * 1000;
    return Date.now() - created <= sevenDays;
  }
}
