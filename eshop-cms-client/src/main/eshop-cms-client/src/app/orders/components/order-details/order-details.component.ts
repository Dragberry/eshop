import { ShippingMethod } from './../../model/shipping-method';
import { PaymentMethod } from './../../model/payment-method';
import { PaymentService } from './../../service/payment.service';
import { NameValue } from './../../../shared/components/table/common/name-value';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap, map } from 'rxjs/operators';
import { OrderService } from '../../service/order.service';
import { OrderDetails } from '../../model/order-details';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { OrderItem } from '../../model/order-item';
import { ConfirmationModalComponent } from 'src/app/shared/components/confirmation-modal/confirmation-modal.component';
import { ShippingService } from '../../service/shipping.service';
import { forkJoin } from 'rxjs';

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
  shippingMethods: ShippingMethod[];

  order: OrderDetails;
  editedOrderitem: OrderItem;
  selectedShippingMethod: ShippingMethod;

  constructor(
    private route: ActivatedRoute,
    private modalService: BsModalService,
    private orderService: OrderService,
    private paymentService: PaymentService,
    private shippingService: ShippingService) {}

  ngOnInit() {
    forkJoin(
      this.paymentService.getAllPaymentMethods(),
      this.shippingService.getAllShippingMethods(),
      this.orderService.fetchOrderStatuses(),
      this.orderService.fetchPaidStatuses())
      .pipe(map(([paymentMethods, shippingMethods, orderStatuses, paidStatuses]) => {
        return {
          paymentMethods, shippingMethods, orderStatuses, paidStatuses
        };
      })).subscribe(data => {
        this.paymentMethods = data.paymentMethods;
        this.shippingMethods = data.shippingMethods;
        this.orderStatuses = data.orderStatuses;
        this.paidStatuses = data.paidStatuses;
        this.route.paramMap.pipe(switchMap((params: ParamMap) => {
          return this.orderService.getOrderDetails(params.get('id'));
        })).subscribe(order => {
          this.order = order;
          this.selectedShippingMethod = this.shippingMethods.find(sm => sm.id === order.shippingMethodId);
        });
      });
  }

  editItem(item: OrderItem): void {
    this.editedOrderitem = {...item};
  }

  isItemEdited(item: OrderItem): boolean {
    return this.editedOrderitem && this.editedOrderitem.id === item.id;
  }

  isItemEnabled(item: OrderItem): boolean {
    return !this.editedOrderitem || this.editedOrderitem.id === item.id;
  }

  onItemEdit(): void {
    this.editedOrderitem.totalAmount = parseFloat((this.editedOrderitem.price * this.editedOrderitem.quantity).toFixed(2));
  }

  saveEditedOrderItem(oldItem: OrderItem): void {
    if (oldItem.price.toString() !== this.editedOrderitem.price.toString()
      || oldItem.quantity.toString() !== this.editedOrderitem.quantity.toString()
      || oldItem.totalAmount.toString() !== this.editedOrderitem.totalAmount.toString()) {
        oldItem.price = this.editedOrderitem.price;
        oldItem.quantity = this.editedOrderitem.quantity;
        oldItem.totalAmount = this.editedOrderitem.totalAmount;
        this.calculateTotalAmount();
        this.updateOrder();
      }
      this.editedOrderitem = null;
  }

  cancelOrderItemEditing(): void {
    this.calculateTotalAmount();
    this.editedOrderitem = null;
  }

  showRemoveOrderItemConfirmation(item: OrderItem) {
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
    this.calculateTotalAmount();
    this.updateOrder();
  }

  onShippingMethodChanged(): void {
    this.order.shippingMethodId = this.selectedShippingMethod.id;
    this.calculateTotalAmount();
    this.updateOrder();
  }

  private calculateTotalAmount(): void {
    let totalAmount = 0;
    this.order.items.forEach(item => {
      totalAmount += item.totalAmount;
    });
    this.order.totalProductAmount = totalAmount;
    this.order.shippingCost = this.selectedShippingMethod.cost;
    this.order.totalAmount = this.order.totalProductAmount + this.order.shippingCost;
  }

  updateOrder(): void {
    this.orderService.updateOrder(this.order).subscribe(result => {
      if (!result.issues) {
        this.order = result.value;
        this.selectedShippingMethod = this.shippingMethods.find(sm => sm.id === this.order.shippingMethodId);
      }
    });
  }
}
