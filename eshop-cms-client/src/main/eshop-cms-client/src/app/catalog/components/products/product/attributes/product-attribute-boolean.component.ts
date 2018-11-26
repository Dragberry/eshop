import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { BooleanAttribute } from 'src/app/catalog/model/attributes';

@Component({
  selector: 'app-product-attribute-boolean',
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
          <div class="form-check">
            <input class="form-check-input" type="checkbox" disabled
              [id]="attribute.id"
              [checked]="attribute.value">
            <label *ngIf="attribute.description" class="form-check-label"
              [for]="attribute.id">
              {{attribute.description}}
            </label>
          </div>
        </div>
      </ng-template>

      <ng-template #attributeIsBeingEdited>
        <div class="col-6">
          {{attribute.name}}
        </div>
        <div class="col-6">
          <div class="form-check">
            <input class="form-check-input" type="checkbox"
              [id]="attribute.id"
              [checked]="attribute.value">
            <label *ngIf="attribute.description" class="form-check-label"
              [for]="attribute.id">
              {{attribute.description}}
            </label>
          </div>
        </div>
      </ng-template>
    </div>
    `
})
export class ProductAttributeBooleanComponent extends AbstractProductAttribute<boolean, BooleanAttribute> {

}
