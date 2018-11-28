import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { ListAttribute } from 'src/app/catalog/model/attributes';

@Component({
  selector: 'app-product-attribute-list',
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
          <ul>
            <li *ngFor="let attrVal of attribute.value">{{attrVal}}</li>
          </ul>
        </div>
      </ng-template>

      <ng-template #attributeIsBeingEdited>
        <div class="col-6">
          <label [for]="'name' + attribute.id" class="font-weight-bold">{{'common.attributeName' | translate}}</label>
          <input [id]="'name' + attribute.id" class="form-control form-control-sm" type="text" [(ngModel)]="attribute.name"/>
        </div>
        <div class="col-6">
          <label [for]="'value' + attribute.id" class="font-weight-bold">
            {{'common.attributeValues' | translate}}
          </label>
          <div class="input-group input-group-sm mb-3">
            <input type="text"
              [id]="'value' + attribute.id" class="form-control"
              [(ngModel)]="valueToAdd"
              (keyup.enter)="addAttributeValue()"/>
            <div class="input-group-append">
              <button class="btn btn-primary" type="button"
                (click)="addAttributeValue()">
                <span class="fa fa-plus"></span>
              </button>
            </div>
          </div>
          <ul class="list-unstyled mt-2">
            <li *ngFor="let val of attribute.value">
              <span class="d-flex">
                <span class="mr-auto">{{val}}</span>
                <button type="button" class="close text-white ml-2"
                  (click)="removeAttributeValue(val)">
                  <span aria-hidden="true">×</span>
                </button>
              </span>
            </li>
          </ul>
        </div>
      </ng-template>
    </div>
    `
})
export class ProductAttributeListComponent extends AbstractProductAttribute<string[], ListAttribute> {

  valueToAdd: string;

  addAttributeValue(): void {
    if (this.attribute.value.find(val => val === this.valueToAdd) == null) {
      this.attribute.value.push(this.valueToAdd);
      this.valueToAdd = null;
    }
  }

  removeAttributeValue(value: string): void {
    this.attribute.value.forEach((item, index) => {
      if (value === item) {
        this.attribute.value.splice(index, 1);
      }
    });
  }

  createAttribute(): ListAttribute {
    return new ListAttribute();
  }

  enrich(src: ListAttribute, dst: ListAttribute): void {
    dst.value = src.value.map(val => val);
  }
}

