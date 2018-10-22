import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Order } from './model/order';

const ORDERS_URL = 'orders';
const ORDERS_LIST_URL = `${ORDERS_URL}/list`;

@Injectable()
export class OrderService {

  constructor(private http: HttpClient) {}

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(ORDERS_LIST_URL);
  }
}
