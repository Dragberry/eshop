import { Page } from './../../shared/model/page';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Order } from '../model/order';

const ORDERS_URL = 'orders';
const ORDERS_LIST_URL = `${ORDERS_URL}/list`;

@Injectable()
export class OrderService {

  constructor(private http: HttpClient) {}

  getOrders(params: HttpParams): Observable<Page<Order>> {
    return this.http.get<Page<Order>>(ORDERS_LIST_URL, { params: params });
  }
}
