import { Observable, from, of } from 'rxjs';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { DataTableState } from 'src/app/shared/model/data-table-state';
import { ProductListState } from './product-list-state';
import { ProductService } from 'src/app/catalog/services/product.service';
import { NavigationService } from 'src/app/core/service/navigation.service';
import { map } from 'rxjs/operators';
import { ProductArticle } from 'src/app/catalog/model/product-article';

@Injectable()
export class ProductListResolverService implements Resolve<ProductListState> {

  state: ProductListState = {
    dataTableState: new DataTableState()
  };

  constructor(
    private navigationService: NavigationService,
    private productService: ProductService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<ProductListState> {
    this.navigationService.currentScreen(state.url, 'products.titles.list');
    if (this.state.dataTableState.initialized) {
      return of(this.state);
    }
    return from(Promise.all([
      this.productService.getCategoryTree()
    ])).pipe(map(result => {
      return {
        dataTableState: new DataTableState<ProductArticle>(),
        categoryTree: result[0]
      };
    }));
  }

  saveState(state: ProductListState) {
    this.state = state;
  }

}
