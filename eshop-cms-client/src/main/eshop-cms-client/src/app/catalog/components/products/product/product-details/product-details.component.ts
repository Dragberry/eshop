import { ActivatedRoute } from '@angular/router';
import { AbstractProductComponent } from './../abstract-product.component';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent extends AbstractProductComponent {

  constructor(protected route: ActivatedRoute) {
    super(route);
  }

}
