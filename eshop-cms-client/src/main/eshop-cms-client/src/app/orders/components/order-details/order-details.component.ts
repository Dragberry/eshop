import { Component } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { OrderService } from '../../service/order.service';
import { OrderDetails } from '../../model/order-details';
import { OrderDetailsEditableComponent } from './order-details-editable.component';
import { MessageService } from 'src/app/core/service/message.service';
import { MessageType } from 'src/app/shared/model/message';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html'
})
export class OrderDetailsComponent extends OrderDetailsEditableComponent {

  editedOrder: OrderDetails;
  editedOrderFields: OrderDetails;

  orderLocked: boolean;

  constructor(
    protected route: ActivatedRoute,
    private messageService: MessageService,
    private orderService: OrderService) {
      super(route);
  }

  lockOrder(orderLocked: boolean) {
    this.orderLocked = orderLocked;
  }

  isOrderCanBeEdited(): boolean {
    return !this.orderLocked && !this.editedOrder && !this.editedOrderFields;
  }

  editOrderDetails(): void {
    this.editedOrder = this.copyOrderDetails(this.order);
  }

  saveEditedOrderDetails(): void {
    this.order = this.copyOrderDetails(this.editedOrder, this.order);
    this.editedOrder = null;
    this.updateOrder();
  }

  cancelOrderEditing(): void {
    this.editedOrder = null;
  }

  isOrderFieldsCanBeEdited(): boolean {
    return !this.orderLocked && !this.editedOrderFields && !this.editedOrder;
  }

  editOrderFields(): void {
    this.editedOrderFields = this.copyOrderDetails(this.order);
  }

  cancelOrderFieldsEditing(): void {
    this.editedOrderFields = null;
  }

  saveEditedOrderFields(): void {
    this.order = this.copyOrderDetails(this.editedOrderFields, this.order);
    this.editedOrderFields = null;
    this.updateOrder();
  }

  private copyOrderDetails(src: OrderDetails, dst?: OrderDetails): OrderDetails {
    if (!dst) {
      dst = new OrderDetails();
    }
    dst.id = src.id;
    dst.orderDate = src.orderDate;
    dst.version = src.version;

    dst.phone = src.phone;
    dst.fullName = src.fullName;
    dst.address = src.address;
    dst.email = src.email;
    dst.comment = src.comment;
    dst.customerComment = src.customerComment;
    dst.shopComment = src.shopComment;
    dst.deliveryDateFrom = src.deliveryDateFrom;
    dst.deliveryDateTo = src.deliveryDateTo;
    return dst;
  }

  updateOrder(): void {
    this.orderService.updateOrder(this.order)
    .then(order => {
      this.order = order;
      this.messageService.showMessage(MessageType.SUCCESS, 'orders.messages.successUpdated', {order: order.id});
    })
    .catch(error => console.log('An error has occured', error));
  }

  downloadReport(): void {
    this.orderService.downloadReport(this.order.id);
  }
}
