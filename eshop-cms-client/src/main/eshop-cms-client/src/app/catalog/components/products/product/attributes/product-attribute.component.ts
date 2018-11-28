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

  /**
   * Created a dynamic component
   * @param attribute - attribute is being passed to component
   * @param isBeingEdited - indicates whether this component is being edited or not
   * @returns a copy of the passed attribute the after the component is created
   */
  private createComponent(attribute: Attribute<any>, isBeingEdited = false): Attribute<any> {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(this.attribute.component);
    const viewContainerRef = this.attributeHost.viewContainerRef;
    viewContainerRef.clear();
    this.componentRef = viewContainerRef.createComponent(componentFactory);
    const copy: Attribute<any> = this.componentRef.instance.copy(attribute);
    this.componentRef.instance.attribute = copy;
    this.componentRef.instance.isBeingEdited = isBeingEdited;
    return copy;
  }

  remove(attribute: Attribute<any>): void {
    this.attributeRemoved.emit(attribute);
  }

  startEditing(attribute: Attribute<any>): void {
    this.editedAttribute = this.createComponent(this.attribute, true);
    this.attributeEditingStarted.emit(attribute);
  }

  finishEditing(): void {
    this.attribute = this.createComponent(this.editedAttribute, false);
    this.attributeEditingFinished.emit(this.attribute);
  }

  cancelEditing(): void {
    this.editedAttribute = null;
    this.createComponent(this.attribute, false);
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
