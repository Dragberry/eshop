package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_ATTRIBUTE_BOOLEAN")
public class ProductAttributeBoolean  extends ProductAttribute<Boolean> {

    @Column(name = "VALUE")
    private Boolean value;
    
    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
}
