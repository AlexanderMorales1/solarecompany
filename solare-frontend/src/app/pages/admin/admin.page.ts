/**
 * @file Panel de administración.
 * @description CRUD de productos y banners, subida de imágenes, filtros de inventario y pedidos.
 * Protegido por `authGuard` y `adminGuard`.
 * @see {@link ../../services/admin.service.ts}
 * @see {@link ../../services/product.service.ts}
 * @see {@link ../../services/order.service.ts}
 * @see {@link ../../services/category.service.ts}
 */

import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HomeBanner } from '../../models/banner.model';
import { Category } from '../../models/category.model';
import { Order } from '../../models/order.model';
import { Product } from '../../models/product.model';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { AdminService } from '../../services/admin.service';
import { CategoryService } from '../../services/category.service';
import { OrderService } from '../../services/order.service';
import { ProductService } from '../../services/product.service';

/** Vista admin: inventario, banners, pedidos y formularios con drag-and-drop de imágenes. */
@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [ReactiveFormsModule, CopCurrencyPipe, DatePipe],
  templateUrl: './admin.page.html',
})
export class AdminPage implements OnInit {
  private readonly allowedImageExtensions = ['jpg', 'jpeg', 'png', 'webp'];
  private readonly productsApi = inject(ProductService);
  private readonly orderApi = inject(OrderService);
  private readonly admin = inject(AdminService);
  private readonly categoriesApi = inject(CategoryService);
  private readonly fb = inject(FormBuilder);

  protected readonly list = signal<Product[]>([]);
  protected readonly banners = signal<HomeBanner[]>([]);
  protected readonly toast = signal<{ type: 'success' | 'error' | 'info'; text: string } | null>(null);
  protected readonly dragOverProductImage = signal(false);
  protected readonly loadingInventory = signal(false);
  protected readonly loadingProductCreate = signal(false);
  protected readonly loadingBannerCreate = signal(false);
  protected readonly loadingCategories = signal(false);
  protected readonly productFile = signal<File | null>(null);
  protected readonly productFileName = signal<string | null>(null);
  protected readonly bannerFile = signal<File | null>(null);
  protected readonly productFilePreview = signal<string | null>(null);
  protected readonly bannerFilePreview = signal<string | null>(null);
  protected readonly editingProductId = signal<number | null>(null);
  protected readonly currentPage = signal(0);
  protected readonly totalPages = signal(0);
  protected readonly totalElements = signal(0);
  protected readonly pageSize = 20;
  protected readonly existingImageUrls = signal<string[]>([]);
  protected readonly categories = signal<Category[]>([]);
  protected readonly editingBannerId = signal<number | null>(null);
  protected readonly orders = signal<Order[]>([]);
  protected readonly ordersLoading = signal(false);
  protected readonly orderCurrentPage = signal(0);
  protected readonly orderTotalPages = signal(0);
  protected readonly orderTotalElements = signal(0);

  protected readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(200)]],
    priceCop: [270000, [Validators.required, Validators.min(0)]],
    description: ['', [Validators.maxLength(5000)]],
    stock: [10, [Validators.required, Validators.min(0)]],
    categorySlug: ['', Validators.required],
    active: [true],
    brandCode: ['FERRATI' as 'FERRATI' | 'RAYBAN'],
    productType: ['CASUAL' as 'CASUAL' | 'DEPORTIVO'],
    gender: ['HOMBRE' as 'HOMBRE' | 'MUJER'],
    featured: [false],
  });

  protected readonly filterForm = this.fb.nonNullable.group({
    q: [''],
    brand: [''],
    gender: [''],
    type: [''],
    category: [''],
  });

  protected readonly bannerForm = this.fb.nonNullable.group({
    title: [''],
    subtitle: [''],
    active: [true],
    displayOrder: [0, [Validators.min(0)]],
  });

  protected readonly orderFilterForm = this.fb.nonNullable.group({
    customer: [''],
    status: [''],
    from: [''],
    to: [''],
  });

  ngOnInit(): void {
    this.reload(0);
    this.reloadBanners();
    this.reloadOrders(0);
    this.loadCategories();
  }

  /** Carga categorías para el selector del formulario de producto. */
  loadCategories(): void {
    this.loadingCategories.set(true);
    this.categoriesApi.list().subscribe({
      next: (items) => {
        this.categories.set(items);
        this.loadingCategories.set(false);
      },
      error: () => {
        this.showToast('error', 'No se pudieron cargar las categorias');
        this.loadingCategories.set(false);
      },
    });
  }

  /** Recarga inventario de productos con filtros y paginación. */
  reload(page = this.currentPage()): void {
    const f = this.filterForm.getRawValue();
    this.loadingInventory.set(true);
    this.productsApi
      .list({
        q: f.q || undefined,
        brand: f.brand || undefined,
        gender: f.gender || undefined,
        type: f.type || undefined,
        category: f.category || undefined,
        sort: 'id,desc',
        page,
        size: this.pageSize,
      })
      .subscribe({
        next: (p) => {
          this.list.set(p.content);
          this.currentPage.set(p.number);
          this.totalPages.set(p.totalPages);
          this.totalElements.set(p.totalElements);
          this.loadingInventory.set(false);
        },
        error: () => {
          this.showToast('error', 'No se pudo cargar inventario');
          this.loadingInventory.set(false);
        },
      });
  }

  /** Lista banners del panel admin. */
  reloadBanners(): void {
    this.admin.listBanners().subscribe({
      next: (items) => this.banners.set(items),
      error: () => {
        this.showToast('error', 'No se pudieron cargar los banners');
      },
    });
  }

  reloadOrders(page = this.orderCurrentPage()): void {
    const f = this.orderFilterForm.getRawValue();
    this.ordersLoading.set(true);
    this.orderApi
      .adminList({
        customer: f.customer || undefined,
        status: f.status || undefined,
        from: f.from || undefined,
        to: f.to || undefined,
        page,
        size: this.pageSize,
      })
      .subscribe({
        next: (res) => {
          this.orders.set(res.content);
          this.orderCurrentPage.set(res.number);
          this.orderTotalPages.set(res.totalPages);
          this.orderTotalElements.set(res.totalElements);
          this.ordersLoading.set(false);
        },
        error: () => {
          this.showToast('error', 'No se pudieron cargar los pedidos');
          this.ordersLoading.set(false);
        },
      });
  }

  onProductImageDragOver(event: DragEvent): void {
    event.preventDefault();
    this.dragOverProductImage.set(true);
  }

  onProductImageDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.dragOverProductImage.set(false);
  }

  onProductImageDrop(event: DragEvent): void {
    event.preventDefault();
    this.dragOverProductImage.set(false);
    const file = event.dataTransfer?.files?.[0];
    if (file) {
      this.setProductImageFile(file);
    }
  }

  onProductFilesSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (file) {
      this.setProductImageFile(file);
    }
  }

  onBannerFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    if (!file) {
      this.bannerFile.set(null);
      this.bannerFilePreview.set(null);
      return;
    }
    if (!this.isAllowedImage(file)) {
      this.showToast('error', 'Formato inválido para banner. Use JPG, JPEG, PNG o WEBP');
      this.bannerFile.set(null);
      this.bannerFilePreview.set(null);
      input.value = '';
      return;
    }
    this.bannerFile.set(file);
    this.bannerFilePreview.set(URL.createObjectURL(file));
  }

  /**
   * Crea o actualiza producto: sube imagen nueva si hay archivo, luego persiste payload.
   * Stock 0 si el producto está marcado como inactivo (`active` false).
   */
  create(): void {
    if (!this.canSubmitProduct()) return;
    const isEditing = this.editingProductId() !== null;
    if (!isEditing && !this.productFile()) {
      this.showToast('error', 'Selecciona la imagen principal del producto');
      return;
    }
    const v = this.form.getRawValue();
    if (!v.categorySlug) {
      this.showToast('error', 'Selecciona una categoria');
      return;
    }
    const slugs = [v.categorySlug];
    const stockToSend = v.active ? v.stock : 0;
    this.loadingProductCreate.set(true);
    // Callback reutilizado tras subir imagen o al editar sin cambiar archivo.
    const persist = (imageUrls: string[]) => {
      const payload = {
        name: v.name,
        brandCode: v.brandCode,
        productType: v.productType,
        gender: v.gender,
        priceCop: v.priceCop,
        description: v.description,
        stock: stockToSend,
        featured: v.featured,
        discountId: null,
        imageUrls,
        categorySlugs: slugs,
      };
      const id = this.editingProductId();
      const req$ = id !== null ? this.admin.updateProduct(id, payload) : this.admin.createProduct(payload);
      req$.subscribe({
        next: () => {
          this.showToast('success', id !== null ? 'Producto actualizado correctamente' : 'Producto creado correctamente');
          this.resetProductForm();
          this.reload(id !== null ? this.currentPage() : 0);
          this.loadingProductCreate.set(false);
        },
        error: (e) => {
          this.showToast('error', e.error?.message ?? 'No se pudo guardar el producto');
          this.loadingProductCreate.set(false);
        },
      });
    };

    const selectedFile = this.productFile();
    if (selectedFile) {
      this.admin.uploadImages([selectedFile], 'products').subscribe({
        next: (uploaded) => persist(uploaded.map((u) => u.url)),
        error: (e) => {
          this.showToast('error', e.error?.message ?? 'Error al subir imagen');
          this.loadingProductCreate.set(false);
        },
      });
      return;
    }

    persist(this.existingImageUrls());
  }

  /** Crea o actualiza banner con FormData (imagen opcional en edición). */
  createBanner(): void {
    if (this.bannerForm.invalid) return;
    const file = this.bannerFile();
    if (!file && this.editingBannerId() === null) {
      this.showToast('error', 'Selecciona una imagen de banner');
      return;
    }
    this.loadingBannerCreate.set(true);
    const v = this.bannerForm.getRawValue();
    const req$ =
      this.editingBannerId() !== null
        ? this.admin.updateBanner(this.editingBannerId()!, {
            title: v.title || undefined,
            subtitle: v.subtitle || undefined,
            active: v.active,
            displayOrder: v.displayOrder,
            imageFile: file ?? undefined,
          })
        : this.admin.createBanner({
            imageFile: file!,
            title: v.title || undefined,
            subtitle: v.subtitle || undefined,
            active: v.active,
            displayOrder: v.displayOrder,
          });

    req$.subscribe({
        next: () => {
          this.showToast('success', this.editingBannerId() !== null ? 'Banner actualizado' : 'Banner creado');
          this.resetBannerForm();
          this.reloadBanners();
          this.loadingBannerCreate.set(false);
        },
        error: (e) => {
          this.showToast('error', e.error?.message ?? 'No se pudo crear el banner');
          this.loadingBannerCreate.set(false);
        },
      });
  }

  editBanner(b: HomeBanner): void {
    this.editingBannerId.set(b.id);
    this.bannerForm.setValue({
      title: b.title ?? '',
      subtitle: b.subtitle ?? '',
      active: b.active,
      displayOrder: b.displayOrder,
    });
    this.bannerFile.set(null);
    this.bannerFilePreview.set(b.imageUrl);
  }

  cancelBannerEdit(): void {
    this.resetBannerForm();
  }

  removeBanner(id: number): void {
    if (!confirm('¿Eliminar banner?')) return;
    this.admin.deleteBanner(id).subscribe({
      next: () => {
        this.showToast('success', 'Banner eliminado');
        this.reloadBanners();
      },
      error: () => {
        this.showToast('error', 'No se pudo eliminar el banner');
      },
    });
  }

  remove(id: number): void {
    if (!confirm('¿Eliminar producto?')) return;
    this.admin.deleteProduct(id).subscribe({
      next: () => {
        this.showToast('success', 'Producto eliminado');
        this.reload(this.currentPage());
      },
      error: () => {
        this.showToast('error', 'No se pudo eliminar');
      },
    });
  }

  editProduct(p: Product): void {
    this.editingProductId.set(p.id);
    this.existingImageUrls.set([...(p.imageUrls ?? [])]);
    this.form.setValue({
      name: p.name,
      brandCode: p.brandCode as 'FERRATI' | 'RAYBAN',
      productType: p.productType as 'CASUAL' | 'DEPORTIVO',
      gender: p.gender as 'HOMBRE' | 'MUJER',
      priceCop: p.priceCop,
      description: p.description ?? '',
      stock: p.stock,
      active: p.stock > 0,
      categorySlug: (p.categorySlugs ?? [])[0] ?? '',
      featured: p.featured,
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit(): void {
    this.resetProductForm();
  }

  applyFilters(): void {
    this.reload(0);
  }

  clearFilters(): void {
    this.filterForm.reset({ q: '', brand: '', gender: '', type: '', category: '' });
    this.reload(0);
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages()) return;
    this.reload(page);
  }

  applyOrderFilters(): void {
    this.reloadOrders(0);
  }

  clearOrderFilters(): void {
    this.orderFilterForm.reset({ customer: '', status: '', from: '', to: '' });
    this.reloadOrders(0);
  }

  goToOrderPage(page: number): void {
    if (page < 0 || page >= this.orderTotalPages()) return;
    this.reloadOrders(page);
  }

  private resetProductForm(): void {
    this.editingProductId.set(null);
    this.productFile.set(null);
    this.productFileName.set(null);
    this.existingImageUrls.set([]);
    this.productFilePreview.set(null);
    this.form.reset({
      name: '',
      priceCop: 270000,
      description: '',
      stock: 10,
      active: true,
      categorySlug: '',
      featured: false,
      brandCode: 'FERRATI',
      productType: 'CASUAL',
      gender: 'HOMBRE',
    });
  }

  private resetBannerForm(): void {
    this.editingBannerId.set(null);
    this.bannerFile.set(null);
    this.bannerFilePreview.set(null);
    this.bannerForm.reset({ title: '', subtitle: '', active: true, displayOrder: 0 });
  }

  private isAllowedImage(file: File): boolean {
    const ext = file.name.includes('.') ? file.name.split('.').pop()?.toLowerCase() ?? '' : '';
    return this.allowedImageExtensions.includes(ext);
  }

  private setProductImageFile(file: File): void {
    if (!this.isAllowedImage(file)) {
      this.showToast('error', 'Formato inválido. Usa JPG, JPEG, PNG o WEBP');
      return;
    }
    this.productFile.set(file);
    this.productFileName.set(file.name);
    this.productFilePreview.set(URL.createObjectURL(file));
  }

  removeProductImage(): void {
    this.productFile.set(null);
    this.productFileName.set(null);
    this.productFilePreview.set(null);
  }

  /** Valida si el formulario de producto puede enviarse (creación exige imagen nueva). */
  canSubmitProduct(): boolean {
    if (this.loadingProductCreate()) return false;
    const v = this.form.getRawValue();
    const requiredBasic =
      !!v.name?.trim() &&
      v.priceCop >= 0 &&
      v.stock >= 0 &&
      !!v.categorySlug &&
      !!this.productFile();
    if (this.editingProductId()) {
      return !!v.name?.trim() && v.priceCop >= 0 && v.stock >= 0 && !!v.categorySlug;
    }
    return requiredBasic;
  }

  /** Muestra notificación temporal que se auto-oculta a los 3 s. */
  private showToast(type: 'success' | 'error' | 'info', text: string): void {
    this.toast.set({ type, text });
    setTimeout(() => {
      if (this.toast()?.text === text) {
        this.toast.set(null);
      }
    }, 3000);
  }
}
