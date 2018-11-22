import { ProductDetailsResolverService } from './components/products/product/product-details/product-details-resolver.service';
import { ProductDetailsComponent } from './components/products/product/product-details/product-details.component';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '../core/auth/auth.guard';
import { ProductListComponent } from './components/products/product-list/product-list.component';
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
                canActivate: [AuthGuard],
                resolve: {
                  data: ProductDetailsResolverService
                }
              }
            ]
          }
        ]
      }
    ])],
    exports: [RouterModule]
})
export class CatalogRoutingModule { }
