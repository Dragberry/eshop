import { Directive, ViewContainerRef, ViewChild } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { Attribute } from 'src/app/catalog/model/attributes';

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[app-product-attribute]',
})
export class ProductAttributeDirective {

  @ViewChild(AbstractProductAttribute)
  component: AbstractProductAttribute<any, Attribute<any>>;

  constructor(public viewContainerRef: ViewContainerRef) { }

}
