import { Component, Input } from '@angular/core';
import { ProductArticleDetails } from 'src/app/catalog/model/product-article-details';

@Component({
  selector: 'app-product-images',
  templateUrl: 'product-images.component.html'
})
export class ProductImagesComponent {

  @Input()
  productArticle: ProductArticleDetails;
}
