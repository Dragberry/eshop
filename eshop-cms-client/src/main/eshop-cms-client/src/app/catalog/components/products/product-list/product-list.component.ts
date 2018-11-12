import { HttpParams } from '@angular/common/http';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { DataTableHolder } from 'src/app/shared/components/table/data-table-holder';
import { ProductArticle } from 'src/app/catalog/model/product-article';
import { ProductService } from 'src/app/catalog/services/product.service';
import { Page } from 'src/app/shared/model/page';
import { ActivatedRoute } from '@angular/router';
import { ProductListState } from './product-list-state';
import { ProductListResolverService } from './product-list-resolver.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent extends DataTableHolder<ProductArticle> implements OnInit, OnDestroy {

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private productResolver: ProductListResolverService ) {
    super();
   }

   ngOnInit() {
    this.route.data.subscribe(routeData => {
      const data = <ProductListState> routeData.data;
      this.setDataTableState(data.dataTableState);
    });
  }

  ngOnDestroy() {
    this.productResolver.saveState({
      dataTableState: this.getDataTableState()
    });
  }

  invokeService(params: HttpParams): Promise<Page<ProductArticle>> {
    return this.productService.getProducts(params);
  }

  loadOptions(productArticle: ProductArticle): void {
    if (productArticle.optionsCount > 1 && productArticle.products == null) {
      this.productService.getProductsForArticle(productArticle.id)
      .then(result => productArticle.products = result);
    }
    productArticle.isProductsShown = !productArticle.isProductsShown;
  }
}
