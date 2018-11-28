import { Type } from '@angular/core';
import { AbstractProductAttribute } from '../components/products/product/attributes/abstract-product-attribute';

export enum AttributeType {
  BOOLEAN = 'BooleanAttribute',
  LIST = 'ListAttribute',
  NUMERIC = 'NumericAttribute',
  STRING = 'StringAttribute',
}

export abstract class Attribute<T> {
  component: Type<AbstractProductAttribute<T, Attribute<T>>>;
  type: AttributeType;
  id: number;
  group: string;
  name: string;
  value: T;
  order: number;
}

export class BooleanAttribute extends Attribute<boolean> {
  description: string;
}

export class ListAttribute extends Attribute<string[]> { }

export class NumericAttribute extends Attribute<number> {
  unit: string;
}

export class StringAttribute extends Attribute<string> { }
