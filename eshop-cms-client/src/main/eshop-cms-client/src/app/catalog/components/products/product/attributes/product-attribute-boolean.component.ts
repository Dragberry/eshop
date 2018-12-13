import { mergeMap } from 'rxjs/operators';
import { Observable, Observer } from 'rxjs';
import { Component } from '@angular/core';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { BooleanAttribute } from 'src/app/catalog/model/attributes';
import { ProductService } from 'src/app/catalog/services/product.service';

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
          <input [id]="'name' + attribute.id" class="form-control form-control-sm" type="text"
            [attr.minlength]="1"
            [attr.maxlength]="64"
            [(ngModel)]="attribute.name"
            [typeahead]="names"
            autocomplete="off"/>
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
            <input type="text" class="form-control"
              [attr.minlength]="1"
              [attr.maxlength]="64"
              [(ngModel)]="attribute.description"
              [typeahead]="values"
              autocomplete="off">
          </div>
        </div>
      </ng-template>
    </div>
    `
})
export class ProductAttributeBooleanComponent extends AbstractProductAttribute<boolean, BooleanAttribute> {

  values: Observable<string>;

  constructor(protected productService: ProductService) {
    super(productService);
    this.values = Observable.create((observer: Observer<string>) => {
      observer.next(this.attribute.description);
    }).pipe(
      mergeMap((token: string) => this.productService.findValuesForAttributes(token, this.attribute.type))
    );
  }
  createAttribute(): BooleanAttribute {
    return new BooleanAttribute();
  }

  enrich(src: BooleanAttribute, dst: BooleanAttribute): void {
    dst.description = src.description;
  }
}
