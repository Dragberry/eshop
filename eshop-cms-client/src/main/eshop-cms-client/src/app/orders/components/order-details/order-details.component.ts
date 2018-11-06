import { ShippingMethod } from './../../model/shipping-method';
import { PaymentMethod } from './../../model/payment-method';
import { PaymentService } from './../../service/payment.service';
import { NameValue } from './../../../shared/components/table/common/name-value';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap, map } from 'rxjs/operators';
import { OrderService } from '../../service/order.service';
import { OrderDetails } from '../../model/order-details';
import { ShippingService } from '../../service/shipping.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html'
})
export class OrderDetailsComponent implements OnInit {

  orderStatuses: NameValue<string>[];
  paidStatuses: NameValue<boolean>[];
  paymentMethods: PaymentMethod[];
  shippingMethods: ShippingMethod[];

  order: OrderDetails;
  editedOrder: OrderDetails;
  editedOrderFields: OrderDetails;

  orderLocked: boolean;

  constructor(
    private route: ActivatedRoute,
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
        });
      });
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
    this.orderService.updateOrder(this.order).subscribe(result => {
      if (!result.issues) {
        this.order = result.value;
      }
    });
  }

  downloadReport(): void {
    this.orderService.downloadReport(this.order.id);
  }
}
