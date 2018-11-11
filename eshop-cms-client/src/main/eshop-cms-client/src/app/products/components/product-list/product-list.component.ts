import { HttpParams } from '@angular/common/http';
import { Page } from './../../../shared/model/page';
import { ProductArticle } from './../../model/product-article';
import { NavigationService } from './../../../core/service/navigation.service';
import { Component } from '@angular/core';
import { DataTableHolder } from 'src/app/shared/components/table/data-table-holder';
import { ProductService } from '../../services/product.service';

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
