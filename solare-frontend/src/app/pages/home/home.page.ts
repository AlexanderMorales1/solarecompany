import { AsyncPipe } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Observable, forkJoin, map } from 'rxjs';
import { HomeBanner } from '../../models/banner.model';
import { Product } from '../../models/product.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { HomeBannerService } from '../../services/home-banner.service';
import { ProductService } from '../../services/product.service';

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
    'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=1600&q=80';
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

  protected nextBanner(): void {
    const items = this.banners();
    if (items.length <= 1) return;
    this.activeBannerIndex.set((this.activeBannerIndex() + 1) % items.length);
    this.startAutoSlide();
  }

  protected previousBanner(): void {
    const items = this.banners();
    if (items.length <= 1) return;
    this.activeBannerIndex.set((this.activeBannerIndex() - 1 + items.length) % items.length);
    this.startAutoSlide();
  }

  protected setBanner(index: number): void {
    if (index < 0 || index >= this.banners().length) return;
    this.activeBannerIndex.set(index);
    this.startAutoSlide();
  }

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

  protected isNewProduct(p: Product): boolean {
    if (!p.createdAt) return false;
    const created = new Date(p.createdAt).getTime();
    if (Number.isNaN(created)) return false;
    const sevenDays = 7 * 24 * 60 * 60 * 1000;
    return Date.now() - created <= sevenDays;
  }
}
