import { mergeMap } from 'rxjs/operators';
import { Observable, Observer } from 'rxjs';
import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { NumericAttribute } from 'src/app/catalog/model/attributes';
import { ProductService } from 'src/app/catalog/services/product.service';

@Component({
  selector: 'app-product-attribute-numeric',
  template: `
    <div class="row">
      <ng-container
        *ngIf="isBeingEdited; then attributeIsBeingEdited; else attributeIsReadonly">
      </ng-container>

      <ng-template #attributeIsReadonly>
        <div class="col-6">
          {{attribute.name}}
        </div>
        <div class="col-6">
          {{attribute.value}}
          {{attribute.unit}}
        </div>
      </ng-template>

      <ng-template #attributeIsBeingEdited>
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
          <label [for]="'value' + attribute.id" class="font-weight-bold">
            {{'common.attributeValue' | translate}}/{{'common.attributeUnit' | translate}}
          </label>
          <div class="input-group input-group-sm">
            <input type="text" class="form-control text-right"
              [attr.minlength]="1"
              [attr.maxlength]="13"
              [(ngModel)]="attribute.value"
              [id]="'value' + attribute.id"
              mask="0*.0*" [dropSpecialCharacters]="false">
            <input type="text" class="form-control"
              [attr.minlength]="0"
              [attr.maxlength]="4"
              [(ngModel)]="attribute.unit"
              [typeahead]="units"
              autocomplete="off">
          </div>
        </div>
      </ng-template>
    </div>
    `
})
export class ProductAttributeNumericComponent extends AbstractProductAttribute<number, NumericAttribute> {

  units: Observable<string>;

  constructor(protected productService: ProductService) {
    super(productService);
    this.units = Observable.create((observer: Observer<string>) => {
      observer.next(this.attribute.unit);
    }).pipe(
      mergeMap((token: string) => this.productService.findValuesForAttributes(token, this.attribute.type))
    );
  }

  createAttribute(): NumericAttribute {
    return new NumericAttribute();
  }

  enrich(src: NumericAttribute, dst: NumericAttribute): void {
    dst.unit = src.unit;
  }
}
