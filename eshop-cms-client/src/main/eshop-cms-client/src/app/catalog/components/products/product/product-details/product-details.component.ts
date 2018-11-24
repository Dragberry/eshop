import { MessageType } from 'src/app/shared/model/message';
import { MessageService } from './../../../../../core/service/message.service';
import { ProductService } from './../../../../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { AbstractProductComponent } from './../abstract-product.component';
import { Component } from '@angular/core';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent extends AbstractProductComponent {

  constructor(
    protected route: ActivatedRoute,
    protected messageService: MessageService,
    protected productService: ProductService) {
    super(route);
  }

  updateProductArticle(): void {
    this.productService.updateProductArticleDetails(this.productArticle)
    .then(productArticle => {
      this.productArticle = productArticle;
      this.messageService.showMessage(MessageType.SUCCESS, 'products.messages.successUpdated', {product: productArticle.title});
    })
    .catch(error => console.log('An error has occured', error));
  }
}
