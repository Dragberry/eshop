import { ProductArticleDetails } from './../../../model/product-article-details';
import { ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';

export class AbstractProductComponent implements OnInit {

  productArticle: ProductArticleDetails;

  constructor(protected route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe(routeData => {
      this.productArticle = routeData.data.product;
    });
  }
}
