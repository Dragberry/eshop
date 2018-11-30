import { Component, Input } from '@angular/core';
import { ProductArticleDetails } from 'src/app/catalog/model';

@Component({
    selector: 'app-product-category-tree',
    templateUrl: './product-category-tree.component.html'
})
export class ProductCategoryTreeComponent {

    @Input()
    productArticle: ProductArticleDetails;
}
