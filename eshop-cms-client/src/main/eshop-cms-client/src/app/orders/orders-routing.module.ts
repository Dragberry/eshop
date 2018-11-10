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
          path: 'new',
          component: OrderCreateComponent,
          canActivate: [AuthGuard]
        },
        {
            path: ':id',
            component: OrderDetailsComponent,
            canActivate: [AuthGuard]
        },
        {
            path: '',
            component: OrderListComponent,
            canActivate: [AuthGuard],
            resolve: {
              data: OrderListResolverService
            }
        }
    ])],
    exports: [RouterModule]
})
export class OrdersRoutingModule { }
