import { Attribute } from 'src/app/catalog/model/attributes';

export abstract class AbstractProductAttribute<V, A extends Attribute<V>> {

  isBeingEdited: boolean;
  attribute: A;

  abstract createAttribute(): A;

  enrich(src: A, dst: A): void { }

  copy(src: A): A {
    const dst = this.createAttribute();
    dst.component = src.component;
    dst.type = src.type;
    dst.id = src.id;
    dst.group = src.group;
    dst.name = src.name;
    dst.order = src.order;
    dst.value = src.value;
    this.enrich(src, dst);
    return dst;
  }
}
