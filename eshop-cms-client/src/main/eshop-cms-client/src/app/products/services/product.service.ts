import { HttpParams } from '@angular/common/http';
import { Page } from './../../shared/model/page';
import { HttpDelegateService } from 'src/app/core/http/http-delegate.service';
import { Injectable } from '@angular/core';
import { ProductArticle } from '../model/product-article';

const PRODUCTS_URL = 'products';

@Injectable()
export class ProductService {

  constructor(private httpService: HttpDelegateService) { }

  getProducts(params: HttpParams): Promise<Page<ProductArticle>> {
    return this.httpService.get<Page<ProductArticle>>(PRODUCTS_URL, { params: params });
  }

}
