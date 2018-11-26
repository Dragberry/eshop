import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { StringAttribute } from 'src/app/catalog/model/attributes';

@Component({
  selector: 'app-product-attribute-string',
  template: `
    <div class="row">
      <div class="col-6">
        {{attribute.name}}
      </div>
      <div class="col-6">
        {{attribute.value}}
      </div>
    </div>
    `
})
export class ProductAttributeStringComponent extends AbstractProductAttribute<string, StringAttribute> {

}
