import { Attribute } from './../../../../model/attributes';
import { Component, Input, ViewChild, ComponentFactoryResolver, OnInit, Output, EventEmitter } from '@angular/core';
import { ProductAttributeDirective } from './product-attribute.directive';
import { AbstractProductAttribute } from './abstract-product-attribute';

@Component({
  selector: 'app-product-attribute',
  templateUrl: './product-attribute.component.html'
})
export class ProductAttributeComponent implements OnInit {

  @ViewChild(ProductAttributeDirective)
  attributeHost: ProductAttributeDirective;

  @Input()
  isEditable: boolean;

  @Input()
  isBeingEdited: boolean;

  @Input()
  attribute: Attribute<any>;

  @Output()
  attributeRemoved: EventEmitter<Attribute<any>> = new EventEmitter();

  @Output()
  attributeEditingStarted: EventEmitter<Attribute<any>> = new EventEmitter();

  @Output()
  attributeEditingFinished: EventEmitter<Attribute<any>> = new EventEmitter();

  @Output()
  attributeEditingCancelled: EventEmitter<Attribute<any>> = new EventEmitter();

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

  startEditing(attribute: Attribute<any>): void {
    this.attributeEditingStarted.emit(attribute);
    this.attributeHost.component.startEditing();
  }

  finishEditing(): void {
    this.attributeEditingFinished.emit(this.attribute);
    this.attributeHost.component.finishEditing();
  }

  cancelEditing(): void {
    this.attributeEditingCancelled.emit(this.attribute);
    this.attributeHost.component.finishEditing();
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
