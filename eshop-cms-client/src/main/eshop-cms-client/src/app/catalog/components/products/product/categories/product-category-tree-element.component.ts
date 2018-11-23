import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProductCategory } from 'src/app/catalog/model/product-category';

@Component({
    selector: 'app-product-category-tree-element',
    template: `
        <ul *ngIf="categoryTree" class="category-block">
            <li *ngFor="let ctg of categoryTree">
              {{ctg.name | translate}}
            <app-product-category-tree-element
                *ngIf="ctg.categories"
                [categoryTree]="ctg.categories">
            </app-product-category-tree-element>
        </li>
    </ul>
    `,
    styles: [
        '.category-block { padding-left: 0.5rem; }'
    ]
})
export class ProductCategoryTreeElementComponent {

    @Input()
    categoryTree: ProductCategory[];
}
