import { NameValue } from './../../../shared/components/table/common/name-value';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { OrderService } from '../../service/order.service';
import { OrderDetails } from '../../model/order-details';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit {

  orderStatuses: NameValue<string>[];
  paidStatuses: NameValue<boolean>[];

  order: OrderDetails;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService) {}

  ngOnInit() {
    this.route.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.orderService.getOrderDetails(params.get('id'));
    })).subscribe(order => this.order = order);
    this.fetchOrderStatuses();
    this.fetchPaidStatuses();
  }

  fetchOrderStatuses(): void {
    this.orderService.fetchOrderStatuses().subscribe(orderStatuses => {
      this.orderStatuses = orderStatuses;
    });
  }

  fetchPaidStatuses(): void {
    this.orderService.fetchPaidStatuses().subscribe(paidStatuses => {
      this.paidStatuses = paidStatuses;
    });
  }
}
