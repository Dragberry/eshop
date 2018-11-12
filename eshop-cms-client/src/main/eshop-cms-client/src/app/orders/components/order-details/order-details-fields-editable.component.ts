import { Component, Input } from '@angular/core';
import { OrderDetails } from '../../model/order-details';

@Component({
  selector: 'app-order-details-fields-editable',
  templateUrl: './order-details-fields-editable.component.html'
})
export class OrderDetailsFieldsEditableComponent {

  @Input()
  order: OrderDetails;

}
