import { ProductAttributeComponent } from './components/products/product/attributes/product-attribute.component';
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
import { ProductAttributesComponent } from './components/products/product/attributes/product-attributes.component';
import { ProductAttributeDirective } from './components/products/product/attributes/product-attribute.directive';
import { ProductAttributeBooleanComponent } from './components/products/product/attributes/product-attribute-boolean.component';
import { ProductAttributeListComponent } from './components/products/product/attributes/product-attribute-list.component';
import { ProductAttributeNumericComponent } from './components/products/product/attributes/product-attribute-numeric.component';
import { ProductAttributeStringComponent } from './components/products/product/attributes/product-attribute-string.component';
import { ProductCreateComponent } from './components/products/product/product-create/product-create.component';
import { ProductCreateResolverService } from './components/products/product/product-create/product-create-resolver.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    CatalogRoutingModule
  ],
  declarations: [
    CatalogComponent,
    CategoryTreeElementComponent,
    ProductAttributeComponent,
    ProductAttributeBooleanComponent,
    ProductAttributeDirective,
    ProductAttributeListComponent,
    ProductAttributeNumericComponent,
    ProductAttributeStringComponent,
    ProductAttributesComponent,
    ProductCreateComponent,
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
    ProductCreateResolverService,
    ProductDetailsResolverService,
    ProductListResolverService
  ],
  entryComponents: [
    ProductAttributeBooleanComponent,
    ProductAttributeListComponent,
    ProductAttributeNumericComponent,
    ProductAttributeStringComponent
  ]
})
export class CatalogModule { }
