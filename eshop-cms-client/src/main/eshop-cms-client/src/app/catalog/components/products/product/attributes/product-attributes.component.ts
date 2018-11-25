import { Attribute } from './../../../../model/attributes';

import { Component, Input, OnDestroy } from '@angular/core';
import { ProductArticleDetails } from 'src/app/catalog/model/product-article-details';
import { DragulaService } from 'ng2-dragula';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-product-attributes',
    templateUrl: './product-attributes.component.html'
})
export class ProductAttributesComponent implements OnDestroy {

  readonly GROUPS: string = 'GROUPS';
  readonly ATTRIBUTES: string = 'ATTRIBUTES';

  dragulaSubscription: Subscription;

  attributes: {group: string, attrs: Attribute<any>[]}[] = [];

  constructor(private dragulaService: DragulaService) {
    this.dragulaService.createGroup(this.GROUPS, {
      invalid: function (el, handle) {
        return el.tagName.toLocaleLowerCase() === 'app-product-attribute';
      }
    });
    this.dragulaSubscription = this.dragulaService.drop()
      .subscribe(() => {
        let groupIndex = 0;
        this.attributes.forEach(attrGroup => {
          let attrIndex = groupIndex;
          attrGroup.attrs.forEach(attr => {
            attr.group = attrGroup.group;
            attr.order = attrIndex++;
          });
          groupIndex += 1000;
        });
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
    this.processAttributes(productArticle.stringAttributes, attributeGroups);
    this.processAttributes(productArticle.booleanAttributes, attributeGroups);
    this.processAttributes(productArticle.numericAttributes, attributeGroups);
    this.processAttributes(productArticle.listAttributes, attributeGroups);
    attributeGroups.forEach((attrs, group) => {
      this.attributes.push({group: group, attrs: attrs});
    });
    this.attributes.sort((group1, group2) => {
      return group1.attrs[0].order - group2.attrs[0].order;
    });
    this.attributes.forEach(group => {
      group.attrs.sort((attr1, attr2) => attr1.order - attr2.order);
    });
  }

  processAttributes(attributeList: Attribute<any>[], attributeGroups: Map<string, Attribute<any>[]>): void {
    attributeList.forEach(attr => {
      let group = attributeGroups.get(attr.group);
      if (!group) {
        group = [];
        attributeGroups.set(attr.group, group);
      }
      group.push(attr);
    });
  }

}
