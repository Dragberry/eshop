export enum AttributeType {
  BOOLEAN = 'org.dragberry.eshop.dal.entity.ProductAttributeBoolean',
  LIST = 'org.dragberry.eshop.dal.entity.ProductAttributeList',
  NUMERIC = 'org.dragberry.eshop.dal.entity.ProductAttributeNumeric',
  STRING = 'org.dragberry.eshop.dal.entity.ProductAttributeString',
}

export const ATTRIBUTE_TYPES: AttributeType[] = [
  AttributeType.BOOLEAN,
  AttributeType.LIST,
  AttributeType.NUMERIC,
  AttributeType.STRING,
];

