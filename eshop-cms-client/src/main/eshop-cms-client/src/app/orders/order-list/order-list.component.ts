import { Page } from './../../shared/model/page';
import { Component, OnInit } from '@angular/core';
import { OrderService } from '../service/order.service';
import { Order } from '../model/order';
import { HttpParams } from '@angular/common/http';
import { DataTableHolder } from '../../shared/components/table/data-table-holder';
import { Observable } from 'rxjs';
import { ShippingService } from '../service/shipping.service';
import { PaymentService } from '../service/payment.service';
import { OrderStatus } from '../model/order-status';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent extends DataTableHolder<Order> implements OnInit {

  paymentMethods: {value: string, name: string}[];
  shippingMethods: {value: string, name: string}[];
  orderStatuses: {value: string, name: string}[];
  paidStatuses: {value: string, name: string}[];

  constructor(private orderService: OrderService,
    private paymentService: PaymentService,
    private shippingService: ShippingService,
    private translateService: TranslateService) {
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
    this.paidStatuses = [];
    [true, false].forEach(value => {
      this.translateService.get(`orders.paid.${value}`).subscribe(translated => {
        this.paidStatuses.push({
          value: String(value),
          name: translated
        });
      });
    });
  }

  fetchOrderStatuses(): void {
    this.orderStatuses = [];
    Object.entries(OrderStatus).forEach(status => {
      this.translateService.get(`orders.status.${status[1]}`).subscribe(translated => {
        this.orderStatuses.push({
          value: status[1],
          name: translated
        });
      });
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
