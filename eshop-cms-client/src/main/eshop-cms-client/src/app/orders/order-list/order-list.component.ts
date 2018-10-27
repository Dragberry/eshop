import { Page } from './../../shared/model/page';
import { Component, OnInit } from '@angular/core';
import { OrderService } from '../order.service';
import { Order } from '../model/order';
import { HttpParams } from '@angular/common/http';
import { DataTableHolder } from '../../shared/components/table/data-table-holder';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent extends DataTableHolder<Order> implements OnInit {

  constructor(private orderService: OrderService) {
    super();
  }

  ngOnInit() {
    this.fetchPage();
  }

  invokeService(params: HttpParams): Observable<Page<Order>> {
    return this.orderService.getOrders(params);
  }

}
