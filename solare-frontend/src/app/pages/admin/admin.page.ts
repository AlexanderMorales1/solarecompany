import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Product } from '../../models/product.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { AdminService } from '../../services/admin.service';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [ReactiveFormsModule, CopCurrencyPipe],
  templateUrl: './admin.page.html',
})
export class AdminPage implements OnInit {
  private readonly productsApi = inject(ProductService);
  private readonly admin = inject(AdminService);
  private readonly fb = inject(FormBuilder);

  protected readonly list = signal<Product[]>([]);
  protected readonly msg = signal<string | null>(null);

  protected readonly form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    brandCode: ['FERRATI' as 'FERRATI' | 'RAYBAN', Validators.required],
    productType: ['CASUAL' as 'CASUAL' | 'DEPORTIVO', Validators.required],
    gender: ['HOMBRE' as 'HOMBRE' | 'MUJER', Validators.required],
    priceCop: [270000, [Validators.required, Validators.min(0)]],
    description: [''],
    stock: [10, [Validators.required, Validators.min(0)]],
    featured: [false],
    imageUrl: ['https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=800&q=80', Validators.required],
    categorySlugs: ['hombre,lentes-casuales'],
  });

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
    this.productsApi.list({ page: 0, size: 100 }).subscribe((p) => this.list.set(p.content));
  }

  create(): void {
    if (this.form.invalid) return;
    const v = this.form.getRawValue();
    const slugs = v.categorySlugs
      .split(',')
      .map((s) => s.trim())
      .filter(Boolean);
    this.admin
      .createProduct({
        name: v.name,
        brandCode: v.brandCode,
        productType: v.productType,
        gender: v.gender,
        priceCop: v.priceCop,
        description: v.description,
        stock: v.stock,
        featured: v.featured,
        discountId: null,
        imageUrls: [v.imageUrl],
        categorySlugs: slugs,
      })
      .subscribe({
        next: () => {
          this.msg.set('Producto creado');
          this.reload();
        },
        error: (e) => this.msg.set(e.error?.message ?? 'Error al crear'),
      });
  }

  remove(id: number): void {
    if (!confirm('¿Eliminar producto?')) return;
    this.admin.deleteProduct(id).subscribe({
      next: () => {
        this.msg.set('Eliminado');
        this.reload();
      },
      error: () => this.msg.set('No se pudo eliminar'),
    });
  }
}
