import { DataTableState } from './../../../shared/model/data-table-state';
import { PaymentMethod } from '../../model/payment-method';
import { ShippingMethod } from '../../model/shipping-method';
import { NameValue } from '../../../shared/components/table/common/name-value';
import { HttpParams } from '@angular/common/http';
import { PaymentService } from '../../service/payment.service';
import { ShippingService } from '../../service/shipping.service';
import { map } from 'rxjs/operators';
import { Observable, from, of } from 'rxjs';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { OrderService } from '../../service/order.service';
import { TitleService } from 'src/app/core/service/title.service';
import { OrderListState } from './order-list-state';

@Injectable()
export class OrderListResolverService implements Resolve<OrderListState> {

  state: OrderListState;

  constructor(
    private orderService: OrderService,
    private paymentService: PaymentService,
    private shippingService: ShippingService,
    private titleService: TitleService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<OrderListState> {
    if (this.state != null) {
      return of(this.state);
    }
    return from(Promise.all([
      this.orderService.getOrders(new HttpParams()
        .set('pageNumber', '1')
        .set('pageSize', '20')),
      this.orderService.fetchPaidStatuses(),
      this.orderService.fetchOrderStatuses(),
      this.paymentService.getActivePaymentMethods(),
      this.shippingService.getActiveShippingMethods()
    ])).pipe(map(result => {
      this.titleService.setTitleKey('orders.titles.list');
      return {
        dataTableState: {
          page: result[0],
        },
        paidStatuses: result[1],
        orderStatuses: result[2],
        paymentMethods: this.mapPaymentMethods(result[3]),
        shippingMethods: this.mapShippingMethods(result[4])
      };
    }));
  }

  saveState(state: OrderListState) {
    this.state = state;
  }

  mapPaymentMethods(paymentMethods: PaymentMethod[]): NameValue<string>[] {
    return paymentMethods.map(sm => {
      return {value: sm.id.toString(), name: sm.name};
    });
  }

  mapShippingMethods(shippingMethods: ShippingMethod[]): NameValue<string>[] {
    return shippingMethods.map(sm => {
      return {value: sm.id.toString(), name: sm.name};
    });
  }
}
