import { ActivatedRouteSnapshot } from '@angular/router';
import { AbstractProductResolverService } from './../abstract-product-resolver.service';
import { Injectable } from '@angular/core';
import { NavigationService } from 'src/app/core/service/navigation.service';
import { ProductService } from 'src/app/catalog/services/product.service';
import { ProductArticleDetails } from 'src/app/catalog/model/product-article-details';

@Injectable()
export class ProductDetailsResolverService extends AbstractProductResolverService {

  constructor(
    protected navigationService: NavigationService,
    protected productService: ProductService) {
    super(navigationService, productService);
  }

  setCurrentScreen(url: string, product: ProductArticleDetails): void {
    this.navigationService.currentScreen(url, 'products.titles.details', {
      id: product.id,
      title: product.title
    });
  }

  getProductDetails(route: ActivatedRouteSnapshot): Promise<ProductArticleDetails> {
    return this.productService.getProductArticleDetails(route.paramMap.get('id'));
  }
}
