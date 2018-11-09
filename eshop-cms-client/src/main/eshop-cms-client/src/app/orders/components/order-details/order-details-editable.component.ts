import { OnInit } from '@angular/core';
import { ShippingService } from './../../service/shipping.service';
import { PaymentService } from './../../service/payment.service';
import { OrderService } from './../../service/order.service';
import { PaymentMethod } from './../../model/payment-method';
import { NameValue } from './../../../shared/components/table/common/name-value';
import { ShippingMethod } from '../../model/shipping-method';
import { OrderDetails } from '../../model/order-details';

export abstract class OrderDetailsEditableComponent implements OnInit {

  orderStatuses: NameValue<string>[];
  paidStatuses: NameValue<boolean>[];
  paymentMethods: PaymentMethod[];
  shippingMethods: ShippingMethod[];

  order: OrderDetails;

  constructor(
    protected orderService: OrderService,
    protected paymentService: PaymentService,
    protected shippingService: ShippingService) {}

    ngOnInit() {
      Promise.all([
        this.paymentService.getAllPaymentMethods(),
        this.shippingService.getAllShippingMethods(),
        this.orderService.fetchOrderStatuses(),
        this.orderService.fetchPaidStatuses()
      ]).then(result => {
        this.paymentMethods = result[0];
        this.shippingMethods = result[1];
        this.orderStatuses = result[2];
        this.paidStatuses = result[3];
        this.fetchOrder();
      });
    }

    abstract fetchOrder(): void;

    abstract updateOrder(): void;
}
