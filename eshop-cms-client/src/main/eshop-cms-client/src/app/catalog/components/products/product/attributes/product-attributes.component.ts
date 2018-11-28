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
    templateUrl: './product-attributes.component.html'
})
export class ProductAttributesComponent implements OnDestroy {

  readonly GROUPS: string = 'GROUPS';
  readonly ATTRIBUTES: string = 'ATTRIBUTES';

  dragulaSubscription: Subscription;

  attributes: {group: string, attrs: Attribute<any>[]}[] = [];
  oldAttributes: {group: string, attrs: Attribute<any>[]}[] = [];

  isBeingEdited: boolean;
  editedAttribute: Attribute<any>;

  constructor(private dragulaService: DragulaService) {
    this.dragulaService.createGroup(this.GROUPS, {
      invalid: function (el, handle) {
        return el.tagName.toLocaleLowerCase() === 'app-product-attribute';
      }
    });
    this.dragulaSubscription = this.dragulaService.drop()
      .subscribe(() => {
        this.calculateOrders();
      });
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

  removeGroup(group: any): void {
    this.attributes.forEach((grp, index) => {
      if (grp === group) {
        this.attributes.splice(index, 1);
      }
    });
    this.calculateOrders();
  }

  removeAttribute(attribute: Attribute<any>): void {
    this.attributes.forEach(group => {
      group.attrs.forEach((attr, index) => {
        if (attribute === attr) {
          group.attrs.splice(index, 1);
        }
      });
    });
    this.calculateOrders();
  }

  startAttributeEditing(attribute: Attribute<any>): void {
    this.editedAttribute = attribute;
  }

  finishAttributeEditing(attribute: Attribute<any>): void {
    const existingGroup = this.attributes.find(grp => grp.group === attribute.group);
    if (!existingGroup) {
      // add a new group if doesnt exist
      this.attributes.push({group: attribute.group, attrs: [attribute]});
    } else {
      const existingAttribute = existingGroup.attrs.find(attr => attr.id === attribute.id);
      if (!existingAttribute) {
        // push a new attribute to the existing group, if such such attribute doesn't exist here
        existingGroup.attrs.push(attribute);
      } else {
        // replace an existing attribute in the existing group
        existingGroup.attrs.forEach((attr, index) => {
          if (attr.id === attribute.id) {
            existingGroup.attrs.splice(index, 1, attribute);
          }
        });
      }
    }
    // deletes atributes whose group doesn't match to the group where they are situated
    this.attributes.forEach((group) => {
      group.attrs.forEach((attr, index) => {
        if (attr.id === attribute.id && attr.group !== attribute.group) {
          group.attrs.splice(index, 1);
        }
      });
    });
    this.calculateOrders();
    this.editedAttribute = null;
  }

  cancelAttributeEditing(attribute: Attribute<any>): void {
    this.editedAttribute = null;
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
