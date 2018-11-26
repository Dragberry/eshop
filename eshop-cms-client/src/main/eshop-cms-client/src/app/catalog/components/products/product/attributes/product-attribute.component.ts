import { Attribute } from './../../../../model/attributes';
import { Component, Input, ViewChild, ComponentFactoryResolver, OnInit, Output, EventEmitter } from '@angular/core';
import { ProductAttributeDirective } from './product-attribute.directive';
import { AbstractProductAttribute } from './abstract-product-attribute';

@Component({
  selector: 'app-product-attribute',
  template: `
      <div class="row pt-2 pb-2 border-bottom"
        [attr.data-id]="attribute.id"
        [attr.data-order]="attribute.order"
        [class.bg-light]="attribute.id === hoveredAttribudeId"
        (mouseover)="hover(attribute.id)"
        (mouseleave)="unhover()">
        <div class="col-9">
          <ng-template app-product-attribute>
          </ng-template>
        </div>
        <div class="col-3">
          <ng-container *ngIf="isBeingEdited">
            <div class="btn-group btn-group-justified">
              <button type="button" class="btn btn-secondary btn-sm"
                [title]="'common.moveUp' | translate"
                (click)="moveUp(attribute)">
                <span class="fa fa-arrow-up fa-sm"></span>
              </button>
              <button type="button" class="btn btn-secondary btn-sm"
                [title]="'common.moveDown' | translate">
                <span class="fa fa-arrow-down fa-sm"
                (click)="moveDown(attribute)"></span>
              </button>
              <button type="button" class="btn btn-warning btn-sm"
                [title]="'common.edit' | translate">
                <span class="fa fa-pencil fa-sm"></span>
              </button>
              <button type="button" class="btn btn-danger btn-sm"
                [title]="'common.delete' | translate"
                (click)="remove(attribute)">
                <span class="fa fa-times fa-sm"></span>
              </button>
            </div>
          </ng-container>
        </div>
      </div>
    `
})
export class ProductAttributeComponent implements OnInit {

  @ViewChild(ProductAttributeDirective)
  attributeHost: ProductAttributeDirective;

  @Input()
  isBeingEdited: boolean;

  @Input()
  attribute: Attribute<any>;

  @Output()
  attributeRemoved: EventEmitter<Attribute<any>> = new EventEmitter();

  @Output()
  movedUp: EventEmitter<Attribute<any>> = new EventEmitter();

  @Output()
  movedDown: EventEmitter<Attribute<any>> = new EventEmitter();

  hoveredAttribudeId: number;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit(): void {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(this.attribute.component);
    const viewContainerRef = this.attributeHost.viewContainerRef;
    viewContainerRef.clear();
    const componentRef = viewContainerRef.createComponent(componentFactory);
    (<AbstractProductAttribute<any, Attribute<any>>>componentRef.instance).attribute = this.attribute;
  }

  remove(attribute: Attribute<any>): void {
    this.attributeRemoved.emit(attribute);
  }

  moveUp(attribute: Attribute<any>): void {
    this.movedUp.emit(attribute);
  }

  moveDown(attribute: Attribute<any>): void {
    this.movedDown.emit(attribute);
  }

  hover(id: number): void {
    this.hoveredAttribudeId = id;
  }

  unhover(): void {
    this.hoveredAttribudeId = null;
  }
}
