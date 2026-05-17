import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { Observable, map, switchMap, tap } from 'rxjs';
import { PageResponse, Product } from '../../models/product.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [RouterLink, AsyncPipe, CopCurrencyPipe, ReactiveFormsModule],
  templateUrl: './catalog.page.html',
})
export class CatalogPage {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly products = inject(ProductService);
  private readonly fb = inject(FormBuilder);

  protected readonly filtersForm = this.fb.nonNullable.group({
    q: [''],
    brand: [''],
    gender: [''],
    type: [''],
    category: [''],
    sort: [''],
  });

  protected readonly page$: Observable<PageResponse<Product>> = this.route.queryParamMap.pipe(
    tap((q) => {
      this.filtersForm.patchValue(
        {
          q: q.get('q') ?? '',
          brand: q.get('brand') ?? '',
          gender: q.get('gender') ?? '',
          type: q.get('type') ?? '',
          category: q.get('category') ?? '',
          sort: q.get('sort') ?? '',
        },
        { emitEvent: false },
      );
    }),
    switchMap((q) =>
      this.products.list({
        q: q.get('q') ?? undefined,
        brand: q.get('brand') ?? undefined,
        gender: q.get('gender') ?? undefined,
        type: q.get('type') ?? undefined,
        category: q.get('category') ?? undefined,
        sort: q.get('sort') ?? undefined,
        page: Number(q.get('page') ?? '0'),
        size: 12,
      }),
    ),
  );

  protected readonly currentPage$ = this.route.queryParamMap.pipe(map((q) => Number(q.get('page') ?? '0')));

  applyFilters(): void {
    const v = this.filtersForm.getRawValue();
    void this.router.navigate(['/tienda'], {
      queryParams: {
        q: v.q || null,
        brand: v.brand || null,
        gender: v.gender || null,
        type: v.type || null,
        category: v.category || null,
        sort: v.sort || null,
        page: 0,
      },
      queryParamsHandling: 'merge',
    });
  }

  clearFilters(): void {
    this.filtersForm.reset({ q: '', brand: '', gender: '', type: '', category: '', sort: '' });
    void this.router.navigate(['/tienda'], {
      queryParams: { q: null, brand: null, gender: null, type: null, category: null, sort: null, page: 0 },
      queryParamsHandling: 'merge',
    });
  }

    goToPage(page: number): void {
      if (page < 0) return;
      void this.router.navigate(['/tienda'], {
        queryParams: { page },
        queryParamsHandling: 'merge',
      });
    }

    getImageUrl(image: string): string {
      if (!image) {
        return 'https://via.placeholder.com/300x300?text=No+Image';
      }

      if (image.startsWith('http')) {
        return image;
      }

      return `http://localhost:8080/api${image}`;
    }
}
