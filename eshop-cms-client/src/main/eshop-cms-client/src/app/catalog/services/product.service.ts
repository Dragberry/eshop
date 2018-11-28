import { ProductArticleDetails } from 'src/app/catalog/model/product-article-details';
import { ProductCategory } from './../model/product-category';
import { HttpParams } from '@angular/common/http';
import { Page } from './../../shared/model/page';
import { HttpDelegateService } from 'src/app/core/http/http-delegate.service';
import { Injectable } from '@angular/core';
import { ProductArticle } from '../model/product-article';
import { ProductOption } from '../model/product-option';

const CATALOG_URL = 'catalog';
const PRODUCTS_URL = `${CATALOG_URL}/products`;
const CATEGORIES_URL = `${CATALOG_URL}/categories`;

@Injectable()
export class ProductService {

  constructor(private httpService: HttpDelegateService) { }

  getProducts(params: HttpParams): Promise<Page<ProductArticle>> {
    return this.httpService.get<Page<ProductArticle>>(PRODUCTS_URL, { params: params });
  }

  getProductsForArticle(articleId: number): Promise<ProductOption[]> {
    return this.httpService.get<ProductOption[]>(`${PRODUCTS_URL}/${articleId}/options`);
  }

  getCategoryTree(): Promise<ProductCategory[]> {
    return this.httpService.get<ProductCategory[]>(`${CATEGORIES_URL}/tree`);
  }

  doImport(): Promise<boolean> {
    return this.httpService.get<boolean>(`${PRODUCTS_URL}/import`);
  }

  getProductArticleDetails(articleId: string): Promise<ProductArticleDetails> {
    return this.httpService.get<ProductArticleDetails>(`${PRODUCTS_URL}/${articleId}`);
  }

  updateProductArticleDetails(product: ProductArticleDetails): Promise<ProductArticleDetails> {
    return this.httpService.put<ProductArticleDetails>(`${PRODUCTS_URL}/${product.id}`, product);
  }

  updateProductAttributes(product: ProductArticleDetails): Promise<ProductArticleDetails> {
    return this.httpService.post<ProductArticleDetails>(`${PRODUCTS_URL}/${product.id}/attributes`, product);
  }
}
