import { ProductCategory } from 'src/app/catalog/model/product-category';
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

  categoryTree: ProductCategory[];

  selectedCategory: ProductCategory;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private productResolver: ProductListResolverService ) {
    super();
   }

   ngOnInit() {
    this.route.data.subscribe(routeData => {
      const data = <ProductListState> routeData.data;
      this.categoryTree = data.categoryTree;
      this.selectedCategory = data.selectedCategory;
      this.setDataTableState(data.dataTableState);
    });
  }

  ngOnDestroy() {
    this.productResolver.saveState({
      dataTableState: this.getDataTableState(),
      categoryTree: this.categoryTree,
      selectedCategory: this.selectedCategory
    });
  }

  invokeService(params: HttpParams): Promise<Page<ProductArticle>> {
    return this.productService.getProducts(params);
  }

  enrichParams(params: HttpParams): HttpParams {
    if (this.selectedCategory && this.selectedCategory.id !== -1) {
      params = params.append('categoryId', this.selectedCategory.id.toString());
    }
    return params;
  }

  loadOptions(productArticle: ProductArticle): void {
    if (productArticle.optionsCount > 1 && productArticle.products == null) {
      this.productService.getProductsForArticle(productArticle.id)
      .then(result => productArticle.products = result);
    }
    productArticle.isProductsShown = !productArticle.isProductsShown;
  }

  selectCategory(category: ProductCategory): void {
    if (this.selectedCategory == null || this.selectedCategory.id !== category.id) {
      this.selectedCategory = category;
    } else {
      this.selectedCategory = null;
    }
    this.fetchPage();
  }

  resetAll(): void {
    this.selectedCategory = null;
    super.resetAll();
  }
}
