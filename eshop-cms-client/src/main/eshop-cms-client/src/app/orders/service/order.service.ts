import { OrderStatus } from './../model/order-status';
import { NameValue } from './../../shared/components/table/common/name-value';
import { Page } from './../../shared/model/page';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Order } from '../model/order';
import { OrderDetails } from '../model/order-details';
import { TranslateService } from '@ngx-translate/core';

const ORDERS_URL = 'orders';
const ORDERS_LIST_URL = `${ORDERS_URL}/list`;
const ORDERS_DETAILS_URL = `${ORDERS_URL}/details`;

@Injectable()
export class OrderService {

  constructor(
    private http: HttpClient,
    private translateService: TranslateService) {}

  fetchOrderStatuses(): Observable<NameValue<string>[]> {
    const orderStatuses = [];
    return new Observable(observer => {
      Object.entries(OrderStatus).forEach(status => {
        this.translateService.get(`orders.status.${status[1]}`).subscribe(translated => {
          orderStatuses.push({
            value: status[1],
            name: translated
          });
        });
      });
      observer.next(orderStatuses);
      observer.complete();
    });
  }

  fetchPaidStatuses(): Observable<NameValue<boolean>[]> {
    const paidStatuses = [];
    return new Observable(observer => {
      [true, false].forEach(value => {
        this.translateService.get(`orders.paid.${value}`).subscribe(translated => {
          paidStatuses.push({
            value: String(value),
            name: translated
          });
        });
      });
      observer.next(paidStatuses);
      observer.complete();
    });
  }

  getOrders(params: HttpParams): Observable<Page<Order>> {
    return this.http.get<Page<Order>>(ORDERS_LIST_URL, { params: params });
  }

  getOrderDetails(id: string): Observable<OrderDetails> {
    return this.http.get<OrderDetails>(`${ORDERS_DETAILS_URL}/${id}`);
  }
}
