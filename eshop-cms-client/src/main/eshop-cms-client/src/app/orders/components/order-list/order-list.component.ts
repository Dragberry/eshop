import { OrderListState } from './order-list-state';
import { OrderListResolverService } from './order-list-resolver.service';
import { ActivatedRoute } from '@angular/router';
import { NameValue } from './../../../shared/components/table/common/name-value';
import { Page } from './../../../shared/model/page';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { OrderService } from '../../service/order.service';
import { Order } from '../../model/order';
import { HttpParams } from '@angular/common/http';
import { DataTableHolder } from '../../../shared/components/table/data-table-holder';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent extends DataTableHolder<Order> implements OnInit, OnDestroy {

  paymentMethods: NameValue<string>[];
  shippingMethods: NameValue<string>[];
  orderStatuses: NameValue<string>[];
  paidStatuses: NameValue<boolean>[];

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private orderResolver: OrderListResolverService) {
    super();
  }

  ngOnInit() {
    this.route.data.subscribe(routeData => {
      const data = <OrderListState> routeData.data;
      this.paidStatuses = data.paidStatuses;
      this.orderStatuses = data.orderStatuses;
      this.paymentMethods = data.paymentMethods;
      this.shippingMethods = data.shippingMethods;
      this.setDataTableState(data.dataTableState);
    });
  }

  ngOnDestroy() {
    this.orderResolver.saveState({
      dataTableState: this.getDataTableState(),
      paidStatuses: this.paidStatuses,
      orderStatuses: this.orderStatuses,
      paymentMethods: this.paymentMethods,
      shippingMethods: this.shippingMethods
    });
  }

  invokeService(params: HttpParams): Promise<Page<Order>> {
    return this.orderService.getOrders(params);
  }

}
