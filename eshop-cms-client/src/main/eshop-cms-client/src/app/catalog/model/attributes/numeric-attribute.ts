import { AttributeType } from './attribute-type';
import { Attribute } from './attribute';

export class NumericAttribute extends Attribute<number> {
  unit: string;

  constructor() {
    super();
    this.type = AttributeType.NUMERIC;
  }
}
