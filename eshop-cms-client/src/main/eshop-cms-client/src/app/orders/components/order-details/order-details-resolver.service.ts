import { NavigationService } from './../../../core/service/navigation.service';
import { ActivatedRouteSnapshot } from '@angular/router';
import { DateService } from 'src/app/core/service/date.service';
import { OrderService } from './../../service/order.service';
import { PaymentService } from './../../service/payment.service';
import { ShippingService } from './../../service/shipping.service';
import { Injectable } from '@angular/core';
import { OrderDetails } from '../../model/order-details';
import { AbstractOrderDetailsResolverService } from './abstract-order-details-resolver.service';

@Injectable()
export class OrderDetailsResolverService extends AbstractOrderDetailsResolverService {

  constructor(
    protected orderService: OrderService,
    protected paymentService: PaymentService,
    protected shippingService: ShippingService,
    protected dateService: DateService,
    protected navigationService: NavigationService) {
      super(orderService, paymentService, shippingService, dateService, navigationService);
  }

  setCurrentScreen(url: string, order: OrderDetails): void {
    this.navigationService.currentScreen(url, 'orders.titles.details', {
      id: order.id,
      date: this.dateService.formatDate(order.orderDate)
    });
  }

  getOrderDetails(route: ActivatedRouteSnapshot): Promise<OrderDetails> {
    return this.orderService.getOrderDetails(route.paramMap.get('id'));
  }

}
