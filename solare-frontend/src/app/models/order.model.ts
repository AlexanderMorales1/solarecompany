/**
 * @file Modelos de pedidos y checkout.
 * @description Tipos para crear pedidos, listar historial y administración.
 * @see {@link ../services/order.service.ts}
 * @see {@link ../pages/checkout/checkout.page.ts}
 */

/** Medios de pago soportados (simulados o integraciones futuras). */
export type PaymentMethod = 'SISTECREDITO' | 'ADDI' | 'BOLD' | 'WOMPI';

/** Cuerpo enviado a `POST /orders/checkout` con datos de envío y pago. */
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

/** Línea de detalle dentro de un pedido confirmado. */
export interface OrderLine {
  productId: number;
  productName: string;
  quantity: number;
  unitPriceCop: number;
}

/** Pedido completo con totales, estado y dirección de entrega. */
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
