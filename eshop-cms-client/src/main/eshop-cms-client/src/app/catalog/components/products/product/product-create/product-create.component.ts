import { AbstractProductComponent } from '../abstract-product.component';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'src/app/core/service/message.service';
import { ProductService } from 'src/app/catalog/services/product.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-product-create',
  templateUrl: './product-create.component.html'
})
export class ProductCreateComponent extends AbstractProductComponent {

  constructor(
    protected route: ActivatedRoute,
    protected messageService: MessageService,
    protected productService: ProductService) {
    super(route);
  }

}
