import { AttributeType } from './attribute-type';

export class Attribute<T> {
  type: AttributeType;
  id: number;
  group: string;
  name: string;
  value: T;
  order: number;
}
