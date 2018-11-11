import { OrdersComponent } from './components/orders.component';
import { OrderCreateResolverService } from './components/order-details/order-create-resolver.service';
import { OrderDetailsResolverService } from './components/order-details/order-details-resolver.service';
import { OrderListResolverService } from './components/order-list/order-list-resolver.service';
import { OrderCreateComponent } from './components/order-details/order-create.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/auth/auth.guard';
import { OrderListComponent } from './components/order-list/order-list.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';

@NgModule({
      imports: [RouterModule.forChild([
        {
          path: '',
          component: OrdersComponent,
          children: [
            {
              path: '',
              component: OrderListComponent,
              canActivate: [AuthGuard],
              resolve: {
                data: OrderListResolverService
              }
            },
            {
              path: 'new',
              component: OrderCreateComponent,
              canActivate: [AuthGuard],
              resolve: {
                data: OrderCreateResolverService
              }
            },
            {
              path: ':id',
              component: OrderDetailsComponent,
              canActivate: [AuthGuard],
              resolve: {
                data: OrderDetailsResolverService
              }
            }
          ]
        }
    ])],
    exports: [RouterModule]
})
export class OrdersRoutingModule { }
