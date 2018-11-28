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
          <label [for]="'name' + attribute.id" class="font-weight-bold">{{'common.attributeName' | translate}}</label>
          <input [id]="'name' + attribute.id" class="form-control form-control-sm" type="text" [(ngModel)]="attribute.name"/>
        </div>
        <div class="col-6">
          <label [for]="'description' + attribute.id" class="font-weight-bold">
            {{'common.attributeValue' | translate}}/{{'common.attributeDescription' | translate}}
          </label>
          <div class="input-group input-group-sm">
            <div class="input-group-prepend">
              <div class="input-group-text">
                <input type="checkbox"
                  [id]="'name' + attribute.id"
                  [checked]="attribute.value"
                  [(ngModel)]="attribute.value">
              </div>
            </div>
            <input type="text" class="form-control" [(ngModel)]="attribute.description">
          </div>
        </div>
      </ng-template>
    </div>
    `
})
export class ProductAttributeBooleanComponent extends AbstractProductAttribute<boolean, BooleanAttribute> {

  createAttribute(): BooleanAttribute {
    return new BooleanAttribute();
  }

  enrich(src: BooleanAttribute, dst: BooleanAttribute): void {
    dst.description = src.description;
  }
}
