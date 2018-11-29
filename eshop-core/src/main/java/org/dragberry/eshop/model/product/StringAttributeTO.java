package org.dragberry.eshop.model.product;

import org.dragberry.eshop.dal.entity.ProductAttributeString;

import lombok.ToString;

@ToString
public class StringAttributeTO extends AttributeTO<String> {

    public StringAttributeTO() {
        super(ProductAttributeString.class);
    }
}
