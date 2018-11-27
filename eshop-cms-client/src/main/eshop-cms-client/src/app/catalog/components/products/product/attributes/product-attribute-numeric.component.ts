import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { NumericAttribute } from 'src/app/catalog/model/attributes';

@Component({
  selector: 'app-product-attribute-numeric',
  template: `
    <div class="row">
      <ng-container
        *ngIf="attribute.isBeingEdited; then attributeIsBeingEdited; else attributeIsReadonly">
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
          <input [id]="'name' + attribute.id" class="form-control form-control-sm" type="text" [(ngModel)]="attribute.name"/>
        </div>
        <div class="col-6">
          <label [for]="'value' + attribute.id" class="font-weight-bold">
            {{'common.attributeValue' | translate}}/{{'common.attributeUnit' | translate}}
          </label>
          <div class="input-group input-group-sm">
            <input type="text" class="form-control text-right"
              [(ngModel)]="attribute.value"
              [id]="'value' + attribute.id"
              mask="0*.0*" [dropSpecialCharacters]="false">
            <input type="text" class="form-control"
              [(ngModel)]="attribute.unit">
          </div>
        </div>
      </ng-template>
    </div>
    `
})
export class ProductAttributeNumericComponent extends AbstractProductAttribute<number, NumericAttribute> {

  copyAttribute(src: NumericAttribute): NumericAttribute {
    return {...src};
  }
}
