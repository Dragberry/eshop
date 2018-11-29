package org.dragberry.eshop.model.product;

import java.util.ArrayList;
import java.util.List;

import org.dragberry.eshop.dal.entity.ProductAttributeList;

import lombok.ToString;

@ToString
public class ListAttributeTO extends AttributeTO<List<String>> {

    public ListAttributeTO() {
        super(ProductAttributeList.class);
    }
    
    @Override
    protected List<String> copyValue() {
        return new ArrayList<>(getValue());
    }
}
