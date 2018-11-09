import { NameValue } from './../../../shared/components/table/common/name-value';
import { Page } from './../../../shared/model/page';
import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../service/order.service';
import { Order } from '../../model/order';
import { HttpParams } from '@angular/common/http';
import { DataTableHolder } from '../../../shared/components/table/data-table-holder';
import { ShippingService } from '../../service/shipping.service';
import { PaymentService } from '../../service/payment.service';
import { TitleService } from 'src/app/core/service/title.service';
import { ScreenComponent } from 'src/app/core/main/screen.component';
import { Observable, from } from 'rxjs';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent extends DataTableHolder<Order> implements OnInit, ScreenComponent {

  paymentMethods: NameValue<string>[];
  shippingMethods: NameValue<string>[];
  orderStatuses: NameValue<string>[];
  paidStatuses: NameValue<boolean>[];

  constructor(private orderService: OrderService,
    private paymentService: PaymentService,
    private shippingService: ShippingService,
    private titleService: TitleService) {
    super();
  }

  ngOnInit() {
    this.titleService.setTitleKey('orders.titles.list');
    this.fetchPaymentMethods();
    this.fetchShippingMethods();
    this.fetchOrderStatuses();
    this.fetchPaidStatuses();
    this.fetchPage();
  }

  getTitle(): Observable<string> {
    return from('orders.titles.list');
  }

  invokeService(params: HttpParams): Promise<Page<Order>> {
    return this.orderService.getOrders(params);
  }

  fetchPaidStatuses(): void {
    this.orderService.fetchPaidStatuses().then(paidStatuses => {
      this.paidStatuses = paidStatuses;
    });
  }

  fetchOrderStatuses(): void {
    this.orderService.fetchOrderStatuses().then(orderStatuses => {
      this.orderStatuses = orderStatuses;
    });
  }

  fetchPaymentMethods(): void {
    this.paymentService.getActivePaymentMethods()
    .then(list => {
      this.paymentMethods = list.map(sm => {
        return {value: sm.id.toString(), name: sm.name};
      });
    });
  }

  fetchShippingMethods(): void {
    this.shippingService.getActiveShippingMethods()
    .then(list => {
      this.shippingMethods = list.map(sm => {
        return {value: sm.id.toString(), name: sm.name};
      });
    });
  }
}
