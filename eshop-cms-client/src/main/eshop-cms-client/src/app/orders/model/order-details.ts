import { OrderItem } from './order-item';

export class OrderDetails {
    id: number;
    orderDate: Date;
    version: number;

    phone: string;
    fullName: string;
    address: string;
    email: string;
    comment: string;
    customerComment: string;
    shopComment: string;
    deliveryDateFrom: Date;
    deliveryDateTo: Date;

    paymentMethodId: number;
    shippingMethodId: number;
    shippingCost: number;
    totalProductAmount: number;
    totalAmount: number;
    paid: boolean;
    status: string;

    items: OrderItem[];
}
