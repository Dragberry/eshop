import { OrderItem } from './order-item';
import { PaymentMethod } from './payment-method';
import { ShippingMethod } from './shipping-method';

export class OrderDetails {
    id: number;
    phone: string;
    totalAmount: number;
    fullName: string;
    address: string;
    comment: string;
    email: string;
    paid: string;
    shippingMethod: ShippingMethod;
    paymentMethod: PaymentMethod;
    date: string;
    status: string;
    version: number;
    items: OrderItem[];
}
