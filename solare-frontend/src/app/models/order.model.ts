export type PaymentMethod = 'SISTECREDITO' | 'ADDI' | 'BOLD';

export interface CheckoutPayload {
  paymentMethod: PaymentMethod;
  fullName: string;
  email: string;
  phoneNumber: string;
  country: string;
  department: string;
  city: string;
  neighborhood: string;
  address: string;
}

export interface OrderLine {
  productId: number;
  productName: string;
  quantity: number;
  unitPriceCop: number;
}

export interface Order {
  id: number;
  totalCop: number;
  status: string;
  paymentMethod: string | null;
  customerFullName?: string | null;
  customerEmail?: string | null;
  customerPhone?: string | null;
  country?: string | null;
  department?: string | null;
  city?: string | null;
  neighborhood?: string | null;
  address?: string | null;
  createdAt: string;
  lines: OrderLine[];
}
