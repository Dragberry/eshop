import { Component, Input } from '@angular/core';
import { ProductArticleDetails } from 'src/app/catalog/model/product-article-details';

@Component({
    selector: 'app-product-description',
    templateUrl: './product-description.component.html'
})
export class ProductDescriptionComponent {

    @Input()
    productArticle: ProductArticleDetails;
}
