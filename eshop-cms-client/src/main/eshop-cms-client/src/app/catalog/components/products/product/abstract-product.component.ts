import { ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { ProductArticleDetails } from 'src/app/catalog/model';

export abstract class AbstractProductComponent implements OnInit {

  state: {
    isDescriptionBeingEdited: false;
    isAttributesBeingEdited: false;
    isImagesBeingEdited: false;
    isCategoryTreeBeingEdited: false;
  };

  productArticle: ProductArticleDetails;

  constructor(protected route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe(routeData => {
      this.productArticle = routeData.data.product;
    });
  }

}
