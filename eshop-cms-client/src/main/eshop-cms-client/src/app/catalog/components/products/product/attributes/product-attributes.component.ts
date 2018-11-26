import { Attribute } from './../../../../model/attributes';

import { Component, Input, OnDestroy, Type } from '@angular/core';
import { ProductArticleDetails } from 'src/app/catalog/model/product-article-details';
import { DragulaService } from 'ng2-dragula';
import { Subscription } from 'rxjs';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { ProductAttributeStringComponent } from './product-attribute-string.component';
import { ProductAttributeBooleanComponent } from './product-attribute-boolean.component';
import { ProductAttributeNumericComponent } from './product-attribute-numeric.component';
import { ProductAttributeListComponent } from './product-attribute-list.component';

@Component({
    selector: 'app-product-attributes',
    template: `
    <ng-container
      *ngIf="isBeingEdited; then editableAttributes; else readonlyAttributes">
    </ng-container>

    <ng-template #readonlyAttributes>
      <div class="card">
        <div class="card-header bg-dark text-white">
          <span>{{ "common.productAttributes" | translate }}</span>
          <button type="button" class="btn btn-dark text-warning pl-2 pt-0 pr-2 pb-0"
            title="{{'common.edit' | translate}}"
            (click)="edit()">
            <span class="fa fa-pencil"></span>
          </button>
        </div>
        <div class="card-body">
          <div class="container">
            <div class="row border-bottom" *ngFor="let group of attributes">
              <div class="col-3">
                {{group.group}}
              </div>
              <div class="col-9">
                <app-product-attribute *ngFor="let attr of group.attrs"
                  [attribute]="attr"
                  [isBeingEdited]="false">
                </app-product-attribute>
              </div>
            </div>
          </div>
        </div>
      </div>
    </ng-template>

    <ng-template #editableAttributes>
      <div class="card">
        <div class="card-header bg-dark text-white">
          <span>{{ "common.productAttributes" | translate }}</span>
          <div class="btn-group btn-group-justified">
            <button type="button" class="btn btn-dark text-primary pl-2 pt-0 pr-2 pb-0"
              title="{{'common.save' | translate}}"
              (click)="save()">
              <span class="fa fa-check"></span>
            </button>
            <button type="button" class="btn btn-dark text-danger pl-2 pt-0 pr-2 pb-0"
              title="{{'common.cancel' | translate}}"
              (click)="cancelEditing()">
              <span class="fa fa-times"></span>
            </button>
          </div>
        </div>
        <div class="card-body">
          <div class="container" [dragula]="GROUPS" [(dragulaModel)]="attributes">
            <div class="row border-bottom" *ngFor="let group of attributes">
              <div class="col-3">
                {{group.group}}
              </div>
              <div class="col-9" [dragula]="ATTRIBUTES" [(dragulaModel)]="group.attrs">
                <app-product-attribute *ngFor="let attr of group.attrs"
                  [attribute]="attr"
                  [isBeingEdited]="true"
                  (attributeRemoved)="removeAttribute($event)"
                  (movedUp)="moveAttributeUp($event)"
                  (movedDown)="moveAttributeDown($event)">
                </app-product-attribute>
              </div>
            </div>
          </div>
        </div>
      </div>
    </ng-template>
    `
})
export class ProductAttributesComponent implements OnDestroy {

  isBeingEdited: boolean;

  readonly GROUPS: string = 'GROUPS';
  readonly ATTRIBUTES: string = 'ATTRIBUTES';

  dragulaSubscription: Subscription;

  attributes: {group: string, attrs: Attribute<any>[]}[] = [];
  oldAttributes: {group: string, attrs: Attribute<any>[]}[] = [];

  constructor(private dragulaService: DragulaService) {
    this.dragulaService.createGroup(this.GROUPS, {
      invalid: function (el, handle) {
        return el.tagName.toLocaleLowerCase() === 'app-product-attribute';
      }
    });
    this.dragulaSubscription = this.dragulaService.drop()
      .subscribe(() => this.calculateOrders());
  }

  ngOnDestroy(): void {
    this.dragulaService.destroy(this.ATTRIBUTES);
    this.dragulaService.destroy(this.GROUPS);
    this.dragulaSubscription.unsubscribe();
  }

  @Input()
  set productArticle(productArticle: ProductArticleDetails) {
    const attributeGroups: Map<string, Attribute<any>[]> = new Map();
    this.processAttributes(productArticle.stringAttributes, attributeGroups, ProductAttributeStringComponent);
    this.processAttributes(productArticle.booleanAttributes, attributeGroups, ProductAttributeBooleanComponent);
    this.processAttributes(productArticle.numericAttributes, attributeGroups, ProductAttributeNumericComponent);
    this.processAttributes(productArticle.listAttributes, attributeGroups, ProductAttributeListComponent);

    attributeGroups.forEach((attrs, group) => {
      this.attributes.push({group: group, attrs: attrs});
    });

    this.attributes.forEach(group => {
      group.attrs.sort((attr1, attr2) => attr1.order - attr2.order);
    });
    this.attributes.sort((group1, group2) => {
      return group1.attrs[0].order - group2.attrs[0].order;
    });

    this.oldAttributes = this.copyAttributes(this.attributes);
  }

  calculateOrders(): void {
    let groupIndex = 0;
    this.attributes.forEach(attrGroup => {
      let attrIndex = groupIndex;
      attrGroup.attrs.forEach(attr => {
        attr.group = attrGroup.group;
        attr.order = attrIndex++;
      });
      groupIndex += 1000;
    });
  }

  copyAttributes(src: {group: string, attrs: Attribute<any>[]}[]): {group: string, attrs: Attribute<any>[]}[] {
    const dst: {group: string, attrs: Attribute<any>[]}[] = [];
    src.forEach(attr => {
      dst.push({
        group: attr.group,
        attrs: attr.attrs.map(a => {
          return {...a};
        })
      });
    });
    return dst;
  }

  processAttributes<T>(
    attributeList: Attribute<T>[],
    attributeGroups: Map<string, Attribute<any>[]>,
    component: Type<AbstractProductAttribute<T, Attribute<T>>>): void {
    attributeList.forEach(attr => {
      let group = attributeGroups.get(attr.group);
      if (!group) {
        group = [];
        attributeGroups.set(attr.group, group);
      }
      attr.component = component;
      group.push(attr);
    });
  }

  edit(): void {
    this.isBeingEdited = true;
  }

  save(): void {
    this.oldAttributes = this.copyAttributes(this.attributes);
    this.isBeingEdited = false;
  }

  cancelEditing(): void {
    this.attributes = this.copyAttributes(this.oldAttributes);
    this.isBeingEdited = false;
  }

  removeAttribute(attribute: Attribute<any>): void {
    this.attributes.forEach((group, groupIndex) => {
      group.attrs.forEach((item, index) => {
        if (attribute === item) {
          group.attrs.splice(index, 1);
        }
      });
      if (group.attrs.length === 0) {
        this.attributes.splice(groupIndex, 1);
      }
    });
  }

  moveAttributeUp(attribute: Attribute<any>): void {
    this.attributes.forEach(group => {
      if (group.attrs.length > 1) {
        for (let i = 0; i < group.attrs.length; i++) {
          if (attribute === group.attrs[i] && i > 0) {
            const temp = group.attrs[i - 1];
            group.attrs[i - 1] = attribute;
            group.attrs[i] = temp;
            break;
          }
        }
      }
    });
    this.calculateOrders();
  }

  moveAttributeDown(attribute: Attribute<any>): void {
    this.attributes.forEach(group => {
      if (group.attrs.length > 1) {
        for (let i = 0; i < group.attrs.length; i++) {
          if (attribute === group.attrs[i] && i < group.attrs.length - 1) {
            const temp = group.attrs[i + 1];
            group.attrs[i + 1] = attribute;
            group.attrs[i] = temp;
            break;
          }
        }
      }
    });
    this.calculateOrders();
  }
}
