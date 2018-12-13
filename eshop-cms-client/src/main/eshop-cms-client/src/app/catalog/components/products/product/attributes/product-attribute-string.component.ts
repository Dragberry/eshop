import { mergeMap } from 'rxjs/operators';
import { Observable, Observer } from 'rxjs';
import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { StringAttribute } from 'src/app/catalog/model/attributes';
import { ProductService } from 'src/app/catalog/services/product.service';

@Component({
  selector: 'app-product-attribute-string',
  template: `
      <ng-container
        *ngIf="isBeingEdited; then attributeIsBeingEdited; else attributeIsReadonly">
      </ng-container>

      <ng-template #attributeIsReadonly>
        <div class="row">
          <div class="col-6">
            {{attribute.name}}
          </div>
          <div class="col-6">
            {{attribute.value}}
          </div>
        </div>
      </ng-template>

      <ng-template #attributeIsBeingEdited>
        <div class="row">
          <div class="col-6">
            <label [for]="'name' + attribute.id" class="font-weight-bold">{{'common.attributeName' | translate}}</label>
            <input [id]="'name' + attribute.id" class="form-control form-control-sm" type="text"
              [attr.minlength]="1"
              [attr.maxlength]="64"
              [(ngModel)]="attribute.name"
              [typeahead]="names"
              autocomplete="off"/>
          </div>
          <div class="col-6">
            <label [for]="'value' + attribute.id" class="font-weight-bold">{{'common.attributeValue' | translate}}</label>
            <input [id]="'value' + attribute.id" class="form-control form-control-sm" type="text"
              [attr.minlength]="1"
              [attr.maxlength]="64"
              [(ngModel)]="attribute.value"
              [typeahead]="values"
              autocomplete="off"/>
          </div>
        </div>
      </ng-template>
    `
})
export class ProductAttributeStringComponent extends AbstractProductAttribute<string, StringAttribute> {

  values: Observable<string>;

  constructor(protected productService: ProductService) {
    super(productService);
    this.values = Observable.create((observer: Observer<string>) => {
      observer.next(this.attribute.value);
    }).pipe(
      mergeMap((token: string) => this.productService.findValuesForAttributes(token, this.attribute.type))
    );
  }

  createAttribute(): StringAttribute {
    return new StringAttribute();
  }

}
