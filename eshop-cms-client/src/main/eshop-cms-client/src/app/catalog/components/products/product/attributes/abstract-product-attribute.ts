import { Attribute } from 'src/app/catalog/model/attributes';

export class AbstractProductAttribute<V, A extends Attribute<V>> {
  attribute: A;
}
