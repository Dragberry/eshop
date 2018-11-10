import { ActivatedRoute } from '@angular/router';
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

  constructor(protected route: ActivatedRoute) {}

    ngOnInit() {
      this.route.data.subscribe(routeData => {
        this.orderStatuses = routeData.data.orderStatuses;
        this.paidStatuses = routeData.data.paidStatuses;
        this.paymentMethods = routeData.data.paymentMethods;
        this.shippingMethods = routeData.data.shippingMethods;
        this.order = routeData.data.order;
      });
    }

    abstract updateOrder(): void;
}
