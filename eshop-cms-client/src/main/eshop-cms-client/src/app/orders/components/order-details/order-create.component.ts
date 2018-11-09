import { TitleService } from 'src/app/core/service/title.service';
import { Router } from '@angular/router';
import { ShippingService } from './../../service/shipping.service';
import { PaymentService } from './../../service/payment.service';
import { OrderService } from './../../service/order.service';
import { Component } from '@angular/core';
import { OrderDetailsEditableComponent } from './order-details-editable.component';
import { OrderDetails } from '../../model/order-details';
import { OrderStatus } from '../../model/order-status';
import { DateService } from 'src/app/core/service/date.service';
import { MessageService } from 'src/app/core/service/message.service';
import { MessageType } from 'src/app/shared/model/message';

@Component({
  selector: 'app-order-create',
  templateUrl: './order-create.component.html'
})
export class OrderCreateComponent extends OrderDetailsEditableComponent {

  constructor(
    private dateService: DateService,
    private messageService: MessageService,
    private router: Router,
    protected orderService: OrderService,
    protected paymentService: PaymentService,
    protected shippingService: ShippingService,
    private titleService: TitleService) {
    super(orderService, paymentService, shippingService);
  }

  fetchOrder(): void {
    this.order = new OrderDetails();
    this.order.items = [];
    this.order.status = OrderStatus.NEW;
    this.order.paid = false;
    this.order.orderDate = new Date();
    this.titleService.setTitleKey('orders.titles.new', {
      date: this.dateService.formatDate(this.order.orderDate)
    });
  }

  updateOrder(): void {
    console.log('Order has been updated');
  }

  saveOrder(): void {
    this.orderService.createOrder(this.order)
    .then(order => {
      this.router.navigate(['/orders/list'])
      .then(() => this.messageService.showMessage(MessageType.SUCCESS, `Success ${order.id}`));
    })
    .catch(error => console.log('An error has occured', error));
  }
}
