import { OrderItem } from './order-item';

export class OrderDetails {
    id: number;
    phone: string;
    totalProductAmount: number;
    shippingCost: number;
    totalAmount: number;
    fullName: string;
    address: string;
    comment: string;
    email: string;
    paid: string;
    shippingMethodId: number;
    paymentMethodId: number;
    date: Date;
    status: string;
    version: number;
    items: OrderItem[];
}
