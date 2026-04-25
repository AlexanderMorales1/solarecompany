export interface Product {
  id: number;
  name: string;
  brandCode: string;
  brandDisplayName: string;
  productType: string;
  gender: string;
  priceCop: number;
  description: string;
  stock: number;
  featured: boolean;
  discountId?: number | null;
  discountCode?: string | null;
  discountType?: string | null;
  imageUrls: string[];
  categorySlugs: string[];
  createdAt?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
