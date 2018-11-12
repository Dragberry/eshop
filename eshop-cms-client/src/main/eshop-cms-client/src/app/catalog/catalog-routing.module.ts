import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '../core/auth/auth.guard';
import { ProductListComponent } from './components/products/product-list/product-list.component';
import { ProductDetailsComponent } from './components/products/product-details/product-details.component';
import { CatalogComponent } from './components/catalog.component';
import { ProductsComponent } from './components/products/products.component';
import { ProductListResolverService } from './components/products/product-list/product-list-resolver.service';

@NgModule({
    imports: [RouterModule.forChild([
      {
        path: '',
        component: CatalogComponent,
        children: [
          {
            path: 'products',
            component: ProductsComponent,
            canActivate: [AuthGuard],
            children: [
              {
                path: '',
                component: ProductListComponent,
                canActivate: [AuthGuard],
                resolve: {
                  data: ProductListResolverService
                }
              },
              {
                path: ':id',
                component: ProductDetailsComponent,
                canActivate: [AuthGuard]
              }
            ]
          }
        ]
      }
    ])],
    exports: [RouterModule]
})
export class CatalogRoutingModule { }
