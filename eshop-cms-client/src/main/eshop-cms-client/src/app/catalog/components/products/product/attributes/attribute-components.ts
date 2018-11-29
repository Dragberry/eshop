import { Type } from '@angular/core';
import { Attribute, AttributeType } from 'src/app/catalog/model/attributes';
import { AbstractProductAttribute } from './abstract-product-attribute';
import { ProductAttributeStringComponent } from './product-attribute-string.component';
import { ProductAttributeNumericComponent } from './product-attribute-numeric.component';
import { ProductAttributeListComponent } from './product-attribute-list.component';
import { ProductAttributeBooleanComponent } from './product-attribute-boolean.component';

const ATTRIBUTE_COMPONENTS: Map<AttributeType, Type<AbstractProductAttribute<any, Attribute<any>>>> = new Map();
ATTRIBUTE_COMPONENTS.set(AttributeType.BOOLEAN, ProductAttributeBooleanComponent);
ATTRIBUTE_COMPONENTS.set(AttributeType.LIST, ProductAttributeListComponent);
ATTRIBUTE_COMPONENTS.set(AttributeType.NUMERIC, ProductAttributeNumericComponent);
ATTRIBUTE_COMPONENTS.set(AttributeType.STRING, ProductAttributeStringComponent);

export {
  ATTRIBUTE_COMPONENTS
};

