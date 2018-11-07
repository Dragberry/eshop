import { Router } from '@angular/router';
import { ShippingService } from './../../service/shipping.service';
import { PaymentService } from './../../service/payment.service';
import { OrderService } from './../../service/order.service';
import { Component } from '@angular/core';
import { OrderDetailsEditableComponent } from './order-details-editable.component';
import { OrderDetails } from '../../model/order-details';
import { OrderStatus } from '../../model/order-status';

@Component({
  selector: 'app-order-create',
  templateUrl: './order-create.component.html'
})
export class OrderCreateComponent extends OrderDetailsEditableComponent {

  constructor(
    private router: Router,
    protected orderService: OrderService,
    protected paymentService: PaymentService,
    protected shippingService: ShippingService) {
    super(orderService, paymentService, shippingService);
  }

  fetchOrder(): void {
    this.order = new OrderDetails();
    this.order.items = [];
    this.order.status = OrderStatus.NEW;
    this.order.paid = false;
    this.order.orderDate = new Date();
  }

  updateOrder(): void {
    console.log('Order has been updated');
  }

  saveOrder(): void {
    this.orderService.createOrder(this.order)
    .then(() => this.router.navigate(['/orders/list']))
    .catch(error => console.log('An error has occured', error));
  }
}
