import { Attribute } from 'src/app/catalog/model/attributes';

export abstract class AbstractProductAttribute<V, A extends Attribute<V>> {

  attribute: A;

  abstract copyAttribute(src: A): A;
}
