import { ProductDetailsResolverService } from './components/products/product/product-details/product-details-resolver.service';
import { ProductDetailsComponent } from './components/products/product/product-details/product-details.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from './services/product.service';
import { CatalogRoutingModule } from './catalog-routing.module';
import { CatalogComponent } from './components/catalog.component';
import { ProductListComponent } from './components/products/product-list/product-list.component';
import { ProductsComponent } from './components/products/products.component';
import { ProductListResolverService } from './components/products/product-list/product-list-resolver.service';
import { SharedModule } from '../shared/shared.module';
import { CategoryTreeElementComponent } from './components/products/product-list/category-tree-element.component';
import { ProductImagesComponent } from './components/products/product/images/product-images.component';
import { ProductCategoryTreeComponent } from './components/products/product/categories/product-category-tree.component';
import { ProductCategoryTreeElementComponent } from './components/products/product/categories/product-category-tree-element.component';
import { ProductDescriptionComponent } from './components/products/product/description/product-description.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    CatalogRoutingModule
  ],
  declarations: [
    CatalogComponent,
    CategoryTreeElementComponent,
    ProductCategoryTreeComponent,
    ProductCategoryTreeElementComponent,
    ProductDescriptionComponent,
    ProductDetailsComponent,
    ProductImagesComponent,
    ProductListComponent,
    ProductsComponent
  ],
  providers: [
    ProductService,
    ProductDetailsResolverService,
    ProductListResolverService
  ]
})
export class CatalogModule { }
