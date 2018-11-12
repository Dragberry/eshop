import { OrderProduct } from './order-product';

export class OrderItem {
  id: number;
  price: number;
  quantity: number;
  totalAmount: number;
  product: OrderProduct;
  version: number;
}
