import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from './services/product.service';
import { CatalogRoutingModule } from './catalog-routing.module';
import { CatalogComponent } from './components/catalog.component';
import { ProductDetailsComponent } from './components/products/product-details/product-details.component';
import { ProductListComponent } from './components/products/product-list/product-list.component';
import { ProductsComponent } from './components/products/products.component';
import { ProductListResolverService } from './components/products/product-list/product-list-resolver.service';
import { SharedModule } from '../shared/shared.module';
import { CategoryTreeElementComponent } from './components/products/product-list/category-tree-element.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    CatalogRoutingModule
  ],
  declarations: [
    CatalogComponent,
    CategoryTreeElementComponent,
    ProductsComponent,
    ProductDetailsComponent,
    ProductListComponent
  ],
  providers: [
    ProductService,
    ProductListResolverService
  ]
})
export class CatalogModule { }
