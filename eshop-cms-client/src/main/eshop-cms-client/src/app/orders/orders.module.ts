import { OrderListResolverService } from './components/order-list/order-list-resolver.service';
import { OrderDetailsFieldsEditableComponent } from './components/order-details/order-details-fields-editable.component';
import { OrderDetailsInfoEditableComponent } from './components/order-details/order-details-info-editable.component';
import { OrderDetailsItemsComponent } from './components/order-details/order-details-items.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderDetailsComponent } from './components/order-details/order-details.component';
import { OrderListComponent } from './components/order-list/order-list.component';
import { OrdersRoutingModule } from './orders-routing.module';
import { SharedModule } from '../shared/shared.module';
import { OrderStatusDirective } from './directives/order-status.directive';
import { OrderService } from './service/order.service';
import { ShippingService } from './service/shipping.service';
import { PaymentService } from './service/payment.service';
import { OrderCreateComponent } from './components/order-details/order-create.component';
import { OrderDetailsResolverService } from './components/order-details/order-details-resolver.service';
import { OrderCreateResolverService } from './components/order-details/order-create-resolver.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    OrdersRoutingModule
  ],
  declarations: [
    OrderCreateComponent,
    OrderListComponent,
    OrderDetailsComponent,
    OrderDetailsFieldsEditableComponent,
    OrderDetailsInfoEditableComponent,
    OrderDetailsItemsComponent,
    OrderStatusDirective
  ],
  providers: [
    OrderService,
    OrderCreateResolverService,
    OrderDetailsResolverService,
    OrderListResolverService,
    PaymentService,
    ShippingService
  ]
})
export class OrdersModule { }
