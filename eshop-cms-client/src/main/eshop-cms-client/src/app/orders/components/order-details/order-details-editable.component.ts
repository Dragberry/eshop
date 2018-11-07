import { map } from 'rxjs/operators';
import { forkJoin } from 'rxjs';
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
      forkJoin(
        this.paymentService.getAllPaymentMethods(),
        this.shippingService.getAllShippingMethods(),
        this.orderService.fetchOrderStatuses(),
        this.orderService.fetchPaidStatuses())
        .pipe(map(([paymentMethods, shippingMethods, orderStatuses, paidStatuses]) => {
          return {
            paymentMethods, shippingMethods, orderStatuses, paidStatuses
          };
        })).subscribe(data => {
          this.paymentMethods = data.paymentMethods;
          this.shippingMethods = data.shippingMethods;
          this.orderStatuses = data.orderStatuses;
          this.paidStatuses = data.paidStatuses;
          this.fetchOrder();
        });
    }

    abstract fetchOrder(): void;

    abstract updateOrder(): void;
}
