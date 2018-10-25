import { Page } from './../../shared/model/page';
import { Component, OnInit } from '@angular/core';
import { OrderService } from '../order.service';
import { Order } from '../model/order';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit {

  orders: Page<Order>;

  pageSize = 20;
  pageNumber = 1;

  constructor(private orderService: OrderService) { }

  ngOnInit() {
    this.fetchOrders();
  }

  fetchOrders(): void {
    this.orderService.getOrders({
      pageNumber: this.pageNumber,
      pageSize: this.pageSize
    }).subscribe(orders => this.orders = orders);
  }

  onPaginator(paginatorEvent: {pageNumber: number, pageSize: number}): void {
    this.pageSize = paginatorEvent.pageSize;
    this.pageNumber = paginatorEvent.pageNumber;
    this.fetchOrders();
  }
}
