import { Directive, ViewContainerRef, ViewChild } from '@angular/core';

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[app-product-attribute]',
})
export class ProductAttributeDirective {

  constructor(public viewContainerRef: ViewContainerRef) { }

}
