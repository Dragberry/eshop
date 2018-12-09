import { Observable } from 'rxjs';
import { ATTRIBUTE_COMPONENTS } from './attribute-components';
import {
  Attribute,
  AttributeType,
  ATTRIBUTE_TYPES,
  BooleanAttribute,
  ListAttribute,
  NumericAttribute,
  StringAttribute
} from 'src/app/catalog/model/attributes';
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
  attribute: Attribute<any>;
  editedAttribute: Attribute<any>;

  @Input()
  isNew: boolean;

  @Input()
  isEditable: boolean;

  @Input()
  isBeingEdited: boolean;

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

  attriubteTypes: AttributeType[] = ATTRIBUTE_TYPES;

  names: Observable<string>;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit(): void {
    if (this.isBeingEdited) {
      this.editedAttribute = this.createComponent(this.attribute, true);
    } else {
      this.createComponent(this.attribute);
    }
  }

  /**
   * Created a dynamic component
   * @param attribute - attribute is being passed to component
   * @param isBeingEdited - indicates whether this component is being edited or not
   * @returns a copy of the passed attribute the after the component is created
   */
  private createComponent(attribute: Attribute<any>, isBeingEdited = false): Attribute<any> {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(ATTRIBUTE_COMPONENTS.get(attribute.type));
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

  changeAttributeType(): void {
    const temp: Attribute<any> = this.editedAttribute;
    switch (this.editedAttribute.type) {
      case AttributeType.BOOLEAN:
        this.editedAttribute = new BooleanAttribute();
        break;
      case AttributeType.LIST:
        this.editedAttribute = new ListAttribute();
        break;
      case AttributeType.NUMERIC:
        this.editedAttribute = new NumericAttribute();
        break;
      case AttributeType.STRING:
        this.editedAttribute = new StringAttribute();
        break;
    }
    this.editedAttribute.id = temp.id;
    this.editedAttribute.group = temp.group;
    this.editedAttribute.name = temp.name;
    this.editedAttribute.order = temp.order;
    this.editedAttribute.type = temp.type;
    this.editedAttribute = this.createComponent(this.editedAttribute, true);
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
