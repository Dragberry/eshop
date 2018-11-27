import { Attribute } from './../../../../model/attributes';
import {
  Component,
  ComponentRef,
  Input,
  ViewChild,
  ComponentFactoryResolver,
  OnInit,
  Output,
  EventEmitter
} from '@angular/core';
import { ProductAttributeDirective } from './product-attribute.directive';
import { AbstractProductAttribute } from './abstract-product-attribute';

@Component({
  selector: 'app-product-attribute',
  templateUrl: './product-attribute.component.html'
})
export class ProductAttributeComponent implements OnInit {

  @ViewChild(ProductAttributeDirective)
  attributeHost: ProductAttributeDirective;

  componentRef: ComponentRef<AbstractProductAttribute<any, Attribute<any>>>;

  @Input()
  isEditable: boolean;

  @Input()
  isBeingEdited: boolean;

  @Input()
  attribute: Attribute<any>;
  editedAttribute: Attribute<any>;

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
    this.createComponent(this.attribute);
  }

  private createComponent(attribute: Attribute<any>) {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(this.attribute.component);
    const viewContainerRef = this.attributeHost.viewContainerRef;
    viewContainerRef.clear();
    this.componentRef = viewContainerRef.createComponent(componentFactory);
    this.componentRef.instance.attribute = attribute;
  }

  remove(attribute: Attribute<any>): void {
    this.attributeRemoved.emit(attribute);
  }

  startEditing(attribute: Attribute<any>): void {
    this.editedAttribute = {...attribute};
    this.editedAttribute.isBeingEdited = true;
    this.createComponent(this.editedAttribute);
    this.attributeEditingStarted.emit(attribute);
  }

  finishEditing(): void {
    this.attribute = {...this.editedAttribute};
    this.attribute.isBeingEdited = false;
    this.createComponent(this.attribute);
    this.attributeEditingFinished.emit(this.attribute);
  }

  cancelEditing(): void {
    this.editedAttribute = null;
    this.createComponent(this.attribute);
    this.attributeEditingCancelled.emit(this.attribute);
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
