import { OrderStatus } from './../model/order-status';
import { NameValue } from './../../shared/components/table/common/name-value';
import { Page } from './../../shared/model/page';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Order } from '../model/order';
import { OrderDetails } from '../model/order-details';
import { TranslateService } from '@ngx-translate/core';
import { Result } from 'src/app/shared/model/result';
import { OrderProduct } from '../model/order-product';

const ORDERS_URL = 'orders';

@Injectable()
export class OrderService {

  constructor(
    private http: HttpClient,
    private translateService: TranslateService) {}

  getOrders(params: HttpParams): Observable<Page<Order>> {
    return this.http.get<Page<Order>>(ORDERS_URL, { params: params });
  }

  getOrderDetails(id: string): Observable<OrderDetails> {
    return this.http.get<OrderDetails>(`${ORDERS_URL}/${id}`);
  }

  updateOrder(order: OrderDetails): Observable<Result<OrderDetails>> {
    return this.http.put<Result<OrderDetails>>(`${ORDERS_URL}/${order.id}`, order);
  }

  searchProducts(query: string): Observable<Page<OrderProduct>> {
    return this.http.get<Page<OrderProduct>>(`products/search`, {
      params: new HttpParams()
        .append('query', query)
        .append('pageSize', '20')
        .append('pageNumber', '1')
    });
  }

  getProductsForArticle(articleId: number): Observable<OrderProduct[]> {
    return this.http.get<OrderProduct[]>(`products/${articleId}/options`);
  }

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

  downloadReport(orderId: number): void {
    this.http.get(`orders/${orderId}/download`, {responseType: 'arraybuffer'})
      .subscribe(response => this.downLoadFile(response, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'));
  }

  /**
   * Method is use to download file.
   * @param data - Array Buffer data
   * @param type - type of the document.
   */
  downLoadFile(data: any, type: string) {
    const blob = new Blob([data], { type: type});
    const url = window.URL.createObjectURL(blob);
    const pwa = window.open(url);
  }
}
