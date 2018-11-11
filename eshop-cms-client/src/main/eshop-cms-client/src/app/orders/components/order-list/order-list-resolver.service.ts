import { NavigationService } from './../../../core/service/navigation.service';
import { Order } from './../../model/order';
import { DataTableState } from './../../../shared/model/data-table-state';
import { PaymentMethod } from '../../model/payment-method';
import { ShippingMethod } from '../../model/shipping-method';
import { NameValue } from '../../../shared/components/table/common/name-value';
import { PaymentService } from '../../service/payment.service';
import { ShippingService } from '../../service/shipping.service';
import { map } from 'rxjs/operators';
import { Observable, from, of } from 'rxjs';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { OrderService } from '../../service/order.service';
import { OrderListState } from './order-list-state';

@Injectable()
export class OrderListResolverService implements Resolve<OrderListState> {

  state: OrderListState = {
    dataTableState: new DataTableState()
  };

  constructor(
    private orderService: OrderService,
    private paymentService: PaymentService,
    private shippingService: ShippingService,
    private navigationService: NavigationService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<OrderListState> {
    this.navigationService.currentScreen(state.url, 'orders.titles.list');
    if (this.state.dataTableState.initialized) {
      return of(this.state);
    }
    return from(Promise.all([
      this.orderService.fetchPaidStatuses(),
      this.orderService.fetchOrderStatuses(),
      this.paymentService.getActivePaymentMethods(),
      this.shippingService.getActiveShippingMethods()
    ])).pipe(map(result => {
      return {
        dataTableState: new DataTableState<Order>(),
        paidStatuses: result[0],
        orderStatuses: result[1],
        paymentMethods: this.mapPaymentMethods(result[2]),
        shippingMethods: this.mapShippingMethods(result[3])
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
