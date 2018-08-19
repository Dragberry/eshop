import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './product-list/product-list.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { AuthGuard } from '../core/auth/auth.guard';

@NgModule({
    imports: [RouterModule.forChild([
        {
            path: 'list',
            component: ProductListComponent,
            canActivate: [AuthGuard]
        },
        {
            path: 'list/details/:id',
            component: ProductDetailsComponent,
            canActivate: [AuthGuard]
        },
        {
            path: '',
            redirectTo: 'list'
        }
    ])],
    exports: [RouterModule]
})
export class ProductsRoutingModule { }
