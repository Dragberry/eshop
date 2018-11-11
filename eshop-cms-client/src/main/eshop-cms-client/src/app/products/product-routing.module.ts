import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '../core/auth/auth.guard';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { ProductsComponent } from './components/products.component';

@NgModule({
    imports: [RouterModule.forChild([
      {
        path: '',
        component: ProductsComponent,
        children: [
          {
            path: '',
            component: ProductListComponent,
            canActivate: [AuthGuard]
          },
          {
            path: ':id',
            component: ProductDetailsComponent,
            canActivate: [AuthGuard]
          },
          {
            path: '',
            redirectTo: 'list'
          }
        ]
      }
    ])],
    exports: [RouterModule]
})
export class ProductsRoutingModule { }
