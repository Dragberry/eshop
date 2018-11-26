import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { ListAttribute } from 'src/app/catalog/model/attributes';

@Component({
  selector: 'app-product-attribute-list',
  template: `
    <div class="row">
      <div class="col-6">
        {{attribute.name}}
      </div>
      <div class="col-6">
        <ul>
          <li *ngFor="let attrVal of attribute.value">{{attrVal}}</li>
        </ul>
      </div>
    </div>
    `
})
export class ProductAttributeListComponent extends AbstractProductAttribute<string[], ListAttribute> {

}
