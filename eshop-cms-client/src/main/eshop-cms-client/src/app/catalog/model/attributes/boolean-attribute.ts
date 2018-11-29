import { Attribute } from './attribute';
import { AttributeType } from './attribute-type';

export class BooleanAttribute extends Attribute<boolean> {
  description: string;

  constructor() {
    super();
    this.type = AttributeType.BOOLEAN;
  }
}
