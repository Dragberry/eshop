import { PaymentMethod } from './../../model/payment-method';
import { PaymentService } from './../../service/payment.service';
import { NameValue } from './../../../shared/components/table/common/name-value';
import { Component, OnInit, TemplateRef } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { OrderService } from '../../service/order.service';
import { OrderDetails } from '../../model/order-details';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { OrderItem } from '../../model/order-item';
import { ConfirmationModalComponent } from 'src/app/shared/components/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit {

  confirmationModalRef: BsModalRef;

  orderStatuses: NameValue<string>[];
  paidStatuses: NameValue<boolean>[];
  paymentMethods: PaymentMethod[];

  order: OrderDetails;

  constructor(
    private route: ActivatedRoute,
    private modalService: BsModalService,
    private orderService: OrderService,
    private paymentService: PaymentService) {}

  ngOnInit() {
    this.route.paramMap.pipe(switchMap((params: ParamMap) => {
      return this.orderService.getOrderDetails(params.get('id'));
    })).subscribe(order => this.order = order);
    this.fetchPaymentMethods();
    this.fetchOrderStatuses();
    this.fetchPaidStatuses();
  }

  fetchPaymentMethods(): void {
    this.paymentService.getAllPaymentMethods()
      .subscribe(list => this.paymentMethods = list);
  }

  fetchOrderStatuses(): void {
    this.orderService.fetchOrderStatuses().subscribe(orderStatuses => {
      this.orderStatuses = orderStatuses;
    });
  }

  fetchPaidStatuses(): void {
    this.orderService.fetchPaidStatuses().subscribe(paidStatuses => {
      this.paidStatuses = paidStatuses;
    });
  }

  showRemoveOrderItemConfirmation(template: TemplateRef<any>, item: OrderItem) {
    this.confirmationModalRef = this.modalService.show(ConfirmationModalComponent, {
      initialState: {
        messageKey: 'orders.messages.confirmRemovingOrderItem',
        onConfirm: () => this.removeOrderItem(item.id)
      }
  });
  }

  removeOrderItem(id: number) {
    this.order.items.forEach((item, index) => {
      if (item.id === id) {
        this.order.items.splice(index, 1);
      }
    });
    this.updateOrder();
  }

  updateOrder(): void {
    this.orderService.updateOrder(this.order).subscribe(result => {
      if (!result.issues) {
        this.order = result.value;
      }
    });
  }
}
