import { PageableEvent } from './../../shared/components/table/common/pageable-event';
import { ColumnActionEvent } from './../../shared/components/table/common/column-action-event';
import { SortDirection } from './../../shared/components/table/common/sort-direction';
import { Page } from './../../shared/model/page';
import { Component, OnInit } from '@angular/core';
import { OrderService } from '../order.service';
import { Order } from '../model/order';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit {

  orders: Page<Order>;

  pageSize = 20;
  pageNumber = 1;

  sortBy: string;
  sortDirection: string;

  filters: Map<string, {name: string, values: string[]}[]> = new Map();

  constructor(private orderService: OrderService) { }

  ngOnInit() {
    this.fetchOrders();
  }

  fetchOrders(): void {
    let params = new HttpParams()
      .set('pageNumber', this.pageNumber.toString())
      .set('pageSize', this.pageSize.toString());
    if (this.sortBy && SortDirection.UNSORTED !== this.sortDirection) {
      params = params.append('sort', `${this.sortBy}[${this.sortDirection}]`);
    }
    this.filters.forEach((fieldFilters) => {
      if (fieldFilters) {
        fieldFilters.filter(filedFilter => filedFilter != null).forEach(filedFilter => {
          if (filedFilter.values) {
            filedFilter.values.filter(value => value != null).forEach(value => {
              params = params.append(filedFilter.name, value);
            });
          }
        });
      }
    });
    this.orderService.getOrders(params).subscribe(orders => this.orders = orders);
  }

  onPaginator(event: PageableEvent): void {
    this.pageSize = event.pageSize;
    this.pageNumber = event.pageNumber;
    this.fetchOrders();
  }

  onColumnAction(event: ColumnActionEvent): void {
    this.sortBy = event.sortBy;
    this.sortDirection = event.sortDirection;
    this.filters.set(event.columnId, event.filterOptions);
    this.fetchOrders();
  }
}
