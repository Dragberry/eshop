import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderListComponent } from './order-list/order-list.component';
import { OrdersRoutingModule } from './orders-routing.module';
import { OrderService } from './order.service';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  imports: [
    CommonModule,
    TranslateModule.forChild(),
    OrdersRoutingModule
  ],
  declarations: [
    OrderListComponent
  ],
  providers: [
    OrderService
  ]
})
export class OrdersModule { }
