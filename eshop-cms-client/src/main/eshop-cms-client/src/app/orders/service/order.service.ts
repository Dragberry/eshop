import { Page } from './../../shared/model/page';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Order } from '../model/order';
import { OrderDetails } from '../model/order-details';

const ORDERS_URL = 'orders';
const ORDERS_LIST_URL = `${ORDERS_URL}/list`;
const ORDERS_DETAILS_URL = `${ORDERS_URL}/details`;

@Injectable()
export class OrderService {

  constructor(private http: HttpClient) {}

  getOrders(params: HttpParams): Observable<Page<Order>> {
    return this.http.get<Page<Order>>(ORDERS_LIST_URL, { params: params });
  }

  getOrderDetails(id: string): Observable<OrderDetails> {
    return this.http.get<OrderDetails>(`${ORDERS_DETAILS_URL}/${id}`);
  }
}
