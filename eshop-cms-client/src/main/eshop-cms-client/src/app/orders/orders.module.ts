import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderListComponent } from './order-list/order-list.component';
import { OrdersRoutingModule } from './orders-routing.module';
import { OrderService } from './order.service';
import { SharedModule } from '../shared/shared.module';
import { OrderStatusDirective } from './directives/order-status.directive';

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
    OrderService
  ]
})
export class OrdersModule { }
