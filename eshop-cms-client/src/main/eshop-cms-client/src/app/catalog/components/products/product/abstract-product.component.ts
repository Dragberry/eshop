import { ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { ProductArticleDetails } from 'src/app/catalog/model';

export abstract class AbstractProductComponent implements OnInit {

  productArticle: ProductArticleDetails;

  constructor(protected route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe(routeData => {
      this.productArticle = routeData.data.product;
    });
  }

  abstract updateProductArticle(): void;
}
