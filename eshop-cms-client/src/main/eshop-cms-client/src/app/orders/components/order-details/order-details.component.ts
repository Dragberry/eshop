import { PaymentMethod } from './../../model/payment-method';
import { PaymentService } from './../../service/payment.service';
import { NameValue } from './../../../shared/components/table/common/name-value';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { OrderService } from '../../service/order.service';
import { OrderDetails } from '../../model/order-details';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit {

  orderStatuses: NameValue<string>[];
  paidStatuses: NameValue<boolean>[];
  paymentMethods: PaymentMethod[];

  order: OrderDetails;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private paymentService: PaymentService) {}

  ngOnInit() {
    this.route.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.orderService.getOrderDetails(params.get('id'));
    })).subscribe(order => this.order = order);
    this.fetchPaymentMethods();
    this.fetchOrderStatuses();
    this.fetchPaidStatuses();
  }

  fetchPaymentMethods(): void {
    this.paymentService.getAllPaymentMethods()
      .subscribe(list => this.paymentMethods = list);
  }

  fetchOrderStatuses(): void {
    this.orderService.fetchOrderStatuses().subscribe(orderStatuses => {
      this.orderStatuses = orderStatuses;
    });
  }

  fetchPaidStatuses(): void {
    this.orderService.fetchPaidStatuses().subscribe(paidStatuses => {
      this.paidStatuses = paidStatuses;
    });
  }
}
