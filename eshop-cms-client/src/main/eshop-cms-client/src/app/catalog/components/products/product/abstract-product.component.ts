import { ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { ProductArticleDetails } from 'src/app/catalog/model';

export abstract class AbstractProductComponent implements OnInit {

  state: Map<string, boolean> = new Map();

  productArticle: ProductArticleDetails;

  constructor(protected route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe(routeData => {
      this.productArticle = routeData.data.product;
    });
  }

  startEditingComponent(component: string): void {
    this.state.clear();
    this.state.set(component, true);
  }

  isComponentBeingEdited(component: string): boolean {
    return this.state.get(component);
  }

}
