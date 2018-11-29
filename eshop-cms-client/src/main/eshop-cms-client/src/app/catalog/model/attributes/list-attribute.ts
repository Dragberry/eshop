import { Attribute } from './attribute';
import { AttributeType } from './attribute-type';

export class ListAttribute extends Attribute<string[]> {
  constructor() {
    super();
    this.type = AttributeType.LIST;
    this.value = [];
  }
}
