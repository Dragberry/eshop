import { ConfirmationModalComponent } from 'src/app/shared/components/confirmation-modal/confirmation-modal.component';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { OrderItem } from './../../model/order-item';
import { OrderDetails } from './../../model/order-details';
import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges,
  SimpleChange,
  ViewChild,
  ViewChildren,
  QueryList
} from '@angular/core';
import { ShippingMethod } from '../../model/shipping-method';
import { BsDropdownDirective } from 'ngx-bootstrap/dropdown';
import { OrderProduct } from '../../model/order-product';
import { OrderService } from '../../service/order.service';

@Component({
  selector: 'app-order-details-items',
  templateUrl: './order-details-items.component.html',
  styleUrls: ['./order-details-items.component.css']
})
export class OrderDetailsItemsComponent implements OnChanges {

  @ViewChild('addOrderItemOptions')
  addOrderItemOptions: BsDropdownDirective;

  @ViewChildren('changeProductOption')
  changeProductOption: QueryList<BsDropdownDirective>;

  confirmationModalRef: BsModalRef;

  @Input()
  shippingMethods: ShippingMethod[];

  @Input()
  order: OrderDetails;

  @Input()
  orderLocked: boolean;

  selectedShippingMethod: ShippingMethod;

  editedOrderItem: OrderItem;

  orderItemBeingAdded: boolean;

  searchProductsDelay: any;
  productSearchQuery: string;
  suggestedProductsList: OrderProduct[];
  suggestedProductsOptions: OrderProduct[];

  @Output()
  orderEdited: EventEmitter<OrderDetails> = new EventEmitter();

  @Output()
  orderBeingEdited: EventEmitter<boolean> = new EventEmitter();

  constructor(
    private modalService: BsModalService,
    private orderService: OrderService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes) {
      const orderChange: SimpleChange = changes['order'];
      if (orderChange && orderChange.currentValue) {
        this.order = orderChange.currentValue;
        this.selectedShippingMethod = this.shippingMethods.find(sm => sm.id === this.order.shippingMethodId);
      }
    }
  }

  isOrderItemCanBeEdited(item: OrderItem): boolean {
    return !this.orderLocked && !this.orderItemBeingAdded && (!this.editedOrderItem || this.editedOrderItem.id === item.id);
  }

  editItem(item: OrderItem): void {
    this.editedOrderItem = {...item};
    this.orderBeingEdited.emit(true);
  }

  isItemEdited(item: OrderItem): boolean {
    return this.editedOrderItem && this.editedOrderItem.id === item.id;
  }

  onItemEdit(): void {
    this.editedOrderItem.totalAmount = parseFloat((this.editedOrderItem.price * this.editedOrderItem.quantity).toFixed(2));
  }

  saveEditedOrderItem(oldItem: OrderItem): void {
    if (oldItem.price.toString() !== this.editedOrderItem.price.toString()
      || oldItem.quantity.toString() !== this.editedOrderItem.quantity.toString()
      || oldItem.totalAmount.toString() !== this.editedOrderItem.totalAmount.toString()) {
        oldItem.price = this.editedOrderItem.price;
        oldItem.quantity = this.editedOrderItem.quantity;
        oldItem.totalAmount = this.editedOrderItem.totalAmount;
        this.calculateTotalAmount();
        this.orderEdited.emit(this.order);
      }
      this.orderBeingEdited.emit(false);
      this.editedOrderItem = null;
    }

  cancelOrderItemEditing(): void {
    this.calculateTotalAmount();
    this.editedOrderItem = null;
    this.orderBeingEdited.emit(false);
  }

  removeOrderItem(id: number) {
    this.order.items.forEach((item, index) => {
      if (item.id === id) {
        this.order.items.splice(index, 1);
      }
    });
    this.calculateTotalAmount();
    this.orderEdited.emit(this.order);
  }

  showRemoveOrderItemConfirmation(item: OrderItem) {
    this.confirmationModalRef = this.modalService.show(ConfirmationModalComponent, {
      initialState: {
        messageKey: 'orders.messages.confirmRemovingOrderItem',
        onConfirm: () => this.removeOrderItem(item.id)
      }
    });
  }

  onShippingMethodChanged(): void {
    this.order.shippingMethodId = this.selectedShippingMethod.id;
    this.calculateTotalAmount();
    this.orderEdited.emit(this.order);
  }

  private calculateTotalAmount(): void {
    let totalAmount = 0;
    this.order.items.forEach(item => {
      totalAmount += item.totalAmount;
    });
    this.order.totalProductAmount = totalAmount;
    this.order.shippingCost = this.selectedShippingMethod ? this.selectedShippingMethod.cost : 0;
    this.order.totalAmount = this.order.totalProductAmount + this.order.shippingCost;
  }

  requestAddingOrderItem(): void {
    this.orderItemBeingAdded = true;
    this.orderBeingEdited.emit(true);
  }

  isOrderItemBeingAdded(): boolean {
    return !this.orderLocked && this.orderItemBeingAdded;
  }

  confirmAddingOrderItem(product: OrderProduct): void {
    const existingItem: OrderItem = this.order.items.find(item => item.product.productId === product.productId);
    if (existingItem) {
      existingItem.quantity += 1;
      existingItem.totalAmount = existingItem.price * existingItem.quantity;
    } else {
      this.order.items.push({
        id: null,
        product: product,
        price: product.actualPrice || product.price,
        quantity: 1,
        totalAmount: product.actualPrice || product.price,
        version: 0
      });
    }
    this.calculateTotalAmount();
    this.orderItemBeingAdded = false;
    this.orderBeingEdited.emit(false);
    this.orderEdited.emit(this.order);
    this.productSearchQuery = null;
  }

  cancelAddingOrderItem(): void {
    this.orderItemBeingAdded = false;
    this.orderBeingEdited.emit(false);
  }

  loadSuggestedProductsList(): void {
    this.addOrderItemOptions.hide();
    clearTimeout(this.searchProductsDelay);
    this.searchProductsDelay = setTimeout(() => {
      if (this.productSearchQuery && this.productSearchQuery.length > 1) {
        this.orderService.searchProducts(this.productSearchQuery).subscribe(page => {
          this.suggestedProductsList = page.content;
          this.addOrderItemOptions.show();
        });
      }
    }, 300);
  }

  loadProductOptions(product: OrderProduct): void {
    this.suggestedProductsOptions = [];
    this.orderService.getProductsForArticle(product.productArticleId).subscribe(result => {
      this.suggestedProductsOptions = result.filter(option => {
        return this.order.items.find(item => item.product.productId === option.productId) == null;
      });
    });
  }

  confirmChangingProduct(item: OrderItem, product: OrderProduct): void {
    item.product = product;
    item.price = product.actualPrice || product.price;
    item.totalAmount = item.price * item.quantity;
    this.calculateTotalAmount();
    this.orderEdited.emit(this.order);
  }
}
