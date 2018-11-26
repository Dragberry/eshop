import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { StringAttribute } from 'src/app/catalog/model/attributes';

@Component({
  selector: 'app-product-attribute-string',
  template: `
    <div class="row">
      <ng-container *ngIf="!isBeingEdited">
        <div class="col-6">
          {{attribute.name}}
        </div>
        <div class="col-6">
          {{attribute.value}}
        </div>
      </ng-container>

      <ng-container *ngIf="isBeingEdited">
        <div class="col-4">
          <input class="form-control" type="text" placeholder="Group" [(ngModel)]="attribute.group">
        </div>
        <div class="col-4">
          <input class="form-control" type="text" placeholder="Name" [(ngModel)]="attribute.name">
        </div>
        <div class="col-4">
          <input class="form-control" type="text" placeholder="Value" [(ngModel)]="attribute.value">
        </div>
      </ng-container>
    </div>
    `
})
export class ProductAttributeStringComponent extends AbstractProductAttribute<string, StringAttribute> {

}
