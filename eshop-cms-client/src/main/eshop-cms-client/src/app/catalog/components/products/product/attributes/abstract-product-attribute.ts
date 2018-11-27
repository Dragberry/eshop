import { Attribute } from 'src/app/catalog/model/attributes';

export abstract class AbstractProductAttribute<V, A extends Attribute<V>> {

  attribute: A;

  startEditing(): void {
    this.attribute.isBeingEdited = true;
  }

  finishEditing(): void {
    this.attribute.isBeingEdited = false;
  }
}
