import { map } from 'rxjs/operators';
import { Observable, from } from 'rxjs';
import { Resolve, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { NavigationService } from 'src/app/core/service/navigation.service';
import { ProductService } from 'src/app/catalog/services/product.service';
import { ProductArticleDetails } from 'src/app/catalog/model';

export abstract class AbstractProductResolverService implements Resolve<ProductArticleDetails> {

  constructor(
    protected navigationService: NavigationService,
    protected productService: ProductService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<any> {
    return from(Promise.all([
      this.getProductDetails(route)
    ])).pipe(map(result => {
      const product: ProductArticleDetails = result[0];
      this.setCurrentScreen(state.url, product);
      return {
        product: product
      };
    }));
  }

  abstract setCurrentScreen(url: string, product: ProductArticleDetails): void;

  abstract getProductDetails(route: ActivatedRouteSnapshot): Promise<ProductArticleDetails>;
}
