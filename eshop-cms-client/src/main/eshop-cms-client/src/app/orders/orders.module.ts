import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderListComponent } from './order-list/order-list.component';
import { OrdersRoutingModule } from './orders-routing.module';
import { SharedModule } from '../shared/shared.module';
import { OrderStatusDirective } from './directives/order-status.directive';
import { OrderService } from './service/order.service';
import { ShippingService } from './service/shipping.service';
import { PaymentService } from './service/payment.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    OrdersRoutingModule
  ],
  declarations: [
    OrderListComponent,
    OrderStatusDirective
  ],
  providers: [
    OrderService,
    PaymentService,
    ShippingService
  ]
})
export class OrdersModule { }
