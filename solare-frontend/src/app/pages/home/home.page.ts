import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Observable, forkJoin, map } from 'rxjs';
import { Product } from '../../models/product.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, AsyncPipe, CopCurrencyPipe],
  templateUrl: './home.page.html',
})
export class HomePage {
  private readonly products = inject(ProductService);

  protected readonly data$: Observable<{ ferrati: Product[]; rayban: Product[] }> = forkJoin({
    ferrati: this.products.featuredByBrand('FERRATI'),
    rayban: this.products.featuredByBrand('RAYBAN'),
  }).pipe(map((d) => d));

  protected readonly bannerSrc =
    'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=1600&q=80';
}
