export type PaymentMethod = 'SISTECREDITO' | 'ADDI' | 'BOLD';

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
  createdAt: string;
  lines: OrderLine[];
}
