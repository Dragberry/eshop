import { NameValue } from './../../../shared/components/table/common/name-value';
import { Page } from './../../../shared/model/page';
import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../service/order.service';
import { Order } from '../../model/order';
import { HttpParams } from '@angular/common/http';
import { DataTableHolder } from '../../../shared/components/table/data-table-holder';
import { Observable } from 'rxjs';
import { ShippingService } from '../../service/shipping.service';
import { PaymentService } from '../../service/payment.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent extends DataTableHolder<Order> implements OnInit {

  paymentMethods: NameValue<string>[];
  shippingMethods: NameValue<string>[];
  orderStatuses: NameValue<string>[];
  paidStatuses: NameValue<boolean>[];

  constructor(private orderService: OrderService,
    private paymentService: PaymentService,
    private shippingService: ShippingService) {
    super();
  }

  ngOnInit() {
    this.fetchPaymentMethods();
    this.fetchShippingMethods();
    this.fetchOrderStatuses();
    this.fetchPaidStatuses();
    this.fetchPage();
  }

  invokeService(params: HttpParams): Observable<Page<Order>> {
    return this.orderService.getOrders(params);
  }

  fetchPaidStatuses(): void {
    this.orderService.fetchPaidStatuses().subscribe(paidStatuses => {
      this.paidStatuses = paidStatuses;
    });
  }

  fetchOrderStatuses(): void {
    this.orderService.fetchOrderStatuses().subscribe(orderStatuses => {
      this.orderStatuses = orderStatuses;
    });
  }

  fetchPaymentMethods(): void {
    this.paymentService.getActivePaymentMethods()
      .subscribe(list => {
        this.paymentMethods = list.map(sm => {
          return {value: sm.id.toString(), name: sm.name};
        });
      });
  }

  fetchShippingMethods(): void {
    this.shippingService.getActiveShippingMethods()
      .subscribe(list => {
        this.shippingMethods = list.map(sm => {
          return {value: sm.id.toString(), name: sm.name};
        });
      });
  }
}
