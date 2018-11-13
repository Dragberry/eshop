import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProductCategory } from 'src/app/catalog/model/product-category';

@Component({
    selector: 'app-category-tree-element',
    template: `
        <ul *ngIf="categoryTree" class="category-block">
            <li *ngFor="let ctg of categoryTree">
            <button class="btn btn-block btn-link pl-2 pt-0 pr-2 pb-0"
                (click)="selectCategory(ctg)"
                [class.btn-link]="selectedCategory?.id !== ctg.id"
                [class.btn-primary]="selectedCategory?.id === ctg.id">
                {{ctg.name | translate}}
            </button>
            <app-category-tree-element
                *ngIf="ctg.categories"
                [selectedCategory]="selectedCategory"
                [categoryTree]="ctg.categories"
                (categorySelected)="selectCategory($event)">
            </app-category-tree-element>
        </li>
    </ul>
    `,
    styles: [
        '.category-block { padding-left: 1rem; }',
        '.category-block button { white-space: normal; text-align: left; font-size: 80%; }'
    ]
})
export class CategoryTreeElementComponent {

    @Input()
    categoryTree: ProductCategory[];

    @Input()
    selectedCategory: ProductCategory;

    @Output()
    categorySelected: EventEmitter<ProductCategory> = new EventEmitter();

    selectCategory(category: ProductCategory): void {
        this.categorySelected.emit(category);
    }
}
