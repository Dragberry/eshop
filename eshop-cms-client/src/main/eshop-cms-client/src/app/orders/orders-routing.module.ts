import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/auth/auth.guard';
import { OrderListComponent } from './components/order-list/order-list.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';

@NgModule({
    imports: [RouterModule.forChild([
        {
            path: 'list',
            component: OrderListComponent,
            canActivate: [AuthGuard]
        },
        {
            path: 'details/:id',
            component: OrderDetailsComponent,
            canActivate: [AuthGuard]
        },
        {
            path: '',
            redirectTo: 'list'
        }
    ])],
    exports: [RouterModule]
})
export class OrdersRoutingModule { }
