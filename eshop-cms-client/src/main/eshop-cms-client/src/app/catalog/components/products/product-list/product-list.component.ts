import { HttpParams } from '@angular/common/http';
import { Component } from '@angular/core';
import { DataTableHolder } from 'src/app/shared/components/table/data-table-holder';
import { ProductArticle } from 'src/app/catalog/model/product-article';
import { NavigationService } from 'src/app/core/service/navigation.service';
import { ProductService } from 'src/app/catalog/services/product.service';
import { Page } from 'src/app/shared/model/page';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent extends DataTableHolder<ProductArticle> {

  constructor(
    private navigationService: NavigationService,
    private productService: ProductService) {
    super();
   }

  invokeService(params: HttpParams): Promise<Page<ProductArticle>> {
    return this.productService.getProducts(params);
  }
}
