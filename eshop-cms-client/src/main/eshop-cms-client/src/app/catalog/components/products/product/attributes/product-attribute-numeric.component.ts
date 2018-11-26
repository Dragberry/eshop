import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { NumericAttribute } from 'src/app/catalog/model/attributes';

@Component({
  selector: 'app-product-attribute-numeric',
  template: `
    <div class="row">
      <div class="col-6">
        {{attribute.name}}
      </div>
      <div class="col-6">
        {{attribute.value}}
        {{attribute.unit}}
      </div>
    </div>
    `
})
export class ProductAttributeNumericComponent extends AbstractProductAttribute<number, NumericAttribute> {

}
