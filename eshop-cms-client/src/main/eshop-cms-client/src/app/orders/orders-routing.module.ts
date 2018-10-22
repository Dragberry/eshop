import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/auth/auth.guard';
import { OrderListComponent } from './order-list/order-list.component';

@NgModule({
    imports: [RouterModule.forChild([
        {
            path: 'list',
            component: OrderListComponent,
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
