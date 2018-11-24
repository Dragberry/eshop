package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.dragberry.eshop.model.product.BooleanAttributeTO;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT_ATTRIBUTE_BOOLEAN")
@Getter
@Setter
public class ProductAttributeBoolean  extends ProductAttribute<Boolean> {

    @Column(name = "VALUE_BOOLEAN")
    private Boolean value;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
    
    public static ProductAttributeBoolean of(ProductArticle productArticle, String group, String name, Boolean value, String description, Integer order) {
    	ProductAttributeBoolean entity = new ProductAttributeBoolean();
    	entity.setProductArticle(productArticle);
    	entity.setGroup(group);
    	entity.setName(name);
    	entity.setValue(value);
    	entity.setDescription(description);
    	entity.setOrder(order);
    	return entity;
    }
    
    public static ProductAttributeBoolean of(ProductArticle productArticle, String group, String name, Boolean value, Integer order) {
    	return of(productArticle, group, name, value, null, order);
    }
    
    @Override
    public String getStringValue() {
    	return description != null ? description : value ? "msg.common.true" : "msg.common.false";
    }
    
    @Override
    public BooleanAttributeTO createTO() {
    	return new BooleanAttributeTO();
    }
    
    @Override
    public BooleanAttributeTO buildTO() {
    	BooleanAttributeTO to = (BooleanAttributeTO) super.buildTO();
    	to.setDescription(description);
    	return to;
    }
    
}
