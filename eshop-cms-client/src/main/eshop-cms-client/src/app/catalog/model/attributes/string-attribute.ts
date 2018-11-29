import { Attribute } from './attribute';
import { AttributeType } from './attribute-type';

export class StringAttribute extends Attribute<string> {
  constructor() {
    super();
    this.type = AttributeType.STRING;
  }
}
