import { OrderDetails } from '../../model/order-details';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-order-details-info-editable',
  templateUrl: './order-details-info-editable.component.html'
})
export class OrderDetailsInfoEditableComponent {

  @Input()
  order: OrderDetails;
}
