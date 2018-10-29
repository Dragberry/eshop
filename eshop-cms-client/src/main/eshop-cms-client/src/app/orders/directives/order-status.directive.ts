import { OrderStatus } from './../model/order-status';
import { Directive, ElementRef, Input, Renderer, OnChanges, SimpleChange, SimpleChanges } from '@angular/core';

@Directive({
  selector: '[appOrderStatus]'
})
export class OrderStatusDirective implements OnChanges {

  @Input()
  orderStatus: OrderStatus;

  constructor(private renderer: Renderer, private el: ElementRef) {
    renderer.setElementClass(el.nativeElement, 'w-100', true);
    renderer.setElementClass(el.nativeElement, 'badge', true);
  }

  ngOnChanges(changes: SimpleChanges) {
    const change: SimpleChange = changes['orderStatus'];
    if (change && change.currentValue) {
      let statusClass;
      switch (change.currentValue) {
        case OrderStatus.NEW:
          statusClass = 'badge-danger';
          break;
        case OrderStatus.PROCESSING:
          statusClass = 'badge-warning';
          break;
        case OrderStatus.AGREED:
          statusClass = 'badge-primary';
          break;
        case OrderStatus.SHIPPED:
          statusClass = 'badge-light';
          break;
        case OrderStatus.DELIVERED:
          statusClass = 'badge-success';
          break;
        case OrderStatus.CANCELLED:
          statusClass = 'badge-dark';
          break;
        default:
          statusClass = 'badge-secondary';
      }
      this.clearClases([
        'badge-danger',
        'badge-warning',
        'badge-primary',
        'badge-light',
        'badge-success',
        'badge-dark',
        'badge-secondary'
      ]);
      this.renderer.setElementClass(this.el.nativeElement, statusClass, true);
    }
  }

  clearClases(classes: string[]) {
    classes.forEach(clazz => this.renderer.setElementClass(this.el.nativeElement, clazz, false));
  }

}
