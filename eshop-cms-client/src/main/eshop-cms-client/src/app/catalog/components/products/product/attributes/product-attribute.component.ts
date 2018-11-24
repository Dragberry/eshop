import { Attribute } from './../../../../model/attributes';
import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-product-attribute',
    template: `
      <div class="row"
        [attr.data-id]="attribute.id"
        [attr.data-order]="attribute.order">
        <div class="col-4">
          {{attribute.name}}
        </div>
        <div class="col-4">
          {{attribute.value}}
        </div>
        <div class="col-4">
          <div class="btn-group btn-group-justified">
            <button type="button" class="btn btn-secondary btn-sm"
              [title]="'common.moveUp' | translate">
              <span class="fa fa-arrow-up fa-sm"></span>
            </button>
            <button type="button" class="btn btn-secondary btn-sm"
              [title]="'common.moveDown' | translate">
              <span class="fa fa-arrow-down fa-sm"></span>
            </button>
            <button type="button" class="btn btn-warning btn-sm"
              [title]="'common.edit' | translate">
              <span class="fa fa-pencil fa-sm"></span>
            </button>
            <button type="button" class="btn btn-danger btn-sm"
              [title]="'common.delete' | translate">
              <span class="fa fa-times fa-sm"></span>
            </button>
          </div>
        </div>
        <div *ngIf="!isLast" class="col-12">
          <hr/>
        </div>
      </div>
    `
})
export class ProductAttributeComponent {

    @Input()
    attribute: Attribute<any>;

    @Input()
    isLast: boolean;
}
