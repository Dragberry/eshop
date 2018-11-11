import { ProductsComponent } from './components/products.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { ProductsRoutingModule } from './product-routing.module';
import { ProductService } from './services/product.service';

@NgModule({
  imports: [
    CommonModule,
    ProductsRoutingModule
  ],
  declarations: [
    ProductsComponent,
    ProductDetailsComponent,
    ProductListComponent
  ],
  providers: [
    ProductService
  ]
})
export class ProductsModule { }
