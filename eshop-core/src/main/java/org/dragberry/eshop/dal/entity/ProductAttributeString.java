package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dragberry.eshop.model.product.StringAttributeTO;

@Entity
@Table(name = "PRODUCT_ATTRIBUTE_STRING")
public class ProductAttributeString  extends ProductAttribute<String> {

    @Column(name = "VALUE_STRING")
    private String value;
    
    public ProductAttributeString() {
       super(StringAttributeTO.class);
    }
    
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    public static ProductAttributeString of(ProductArticle productArticle, String group, String name, String value, Integer order) {
    	ProductAttributeString entity = new ProductAttributeString();
    	entity.setProductArticle(productArticle);
    	entity.setGroup(group);
    	entity.setName(name);
    	entity.setValue(value);
    	entity.setOrder(order);
    	return entity;
    }
    
    @Override
    public String getStringValue() {
    	return value;
    }

}
