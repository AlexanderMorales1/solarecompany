import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Observable, switchMap } from 'rxjs';
import { PageResponse, Product } from '../../models/product.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [RouterLink, AsyncPipe, CopCurrencyPipe],
  templateUrl: './catalog.page.html',
})
export class CatalogPage {
  private readonly route = inject(ActivatedRoute);
  private readonly products = inject(ProductService);

  protected readonly page$: Observable<PageResponse<Product>> = this.route.queryParamMap.pipe(
    switchMap((q) =>
      this.products.list({
        brand: q.get('brand') ?? undefined,
        gender: q.get('gender') ?? undefined,
        type: q.get('type') ?? undefined,
        category: q.get('category') ?? undefined,
        page: Number(q.get('page') ?? '0'),
        size: 12,
      }),
    ),
  );
}
