import { NavigationService } from './../../../core/service/navigation.service';
import { DateService } from 'src/app/core/service/date.service';
import { map } from 'rxjs/operators';
import { OrderService } from '../../service/order.service';
import { PaymentService } from '../../service/payment.service';
import { ShippingService } from '../../service/shipping.service';
import { Observable, from } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { OrderDetails } from '../../model/order-details';

export abstract class AbstractOrderDetailsResolverService implements Resolve<any> {

  constructor(
    protected orderService: OrderService,
    protected paymentService: PaymentService,
    protected shippingService: ShippingService,
    protected dateService: DateService,
    protected navigationService: NavigationService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<any> {
    return from(Promise.all([
      this.paymentService.getAllPaymentMethods(),
      this.shippingService.getAllShippingMethods(),
      this.orderService.fetchOrderStatuses(),
      this.orderService.fetchPaidStatuses(),
      this.getOrderDetails(route)
    ])).pipe(map(result => {
      const order: OrderDetails = result[4];
      this.setCurrentScreen(state.url, order);
      return {
        paymentMethods: result[0],
        shippingMethods: result[1],
        orderStatuses: result[2],
        paidStatuses: result[3],
        order: order
      };
    }));
  }

  abstract setCurrentScreen(url: string, order: OrderDetails): void;

  abstract getOrderDetails(route: ActivatedRouteSnapshot): Promise<OrderDetails>;

}
