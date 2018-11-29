package org.dragberry.eshop.model.product;

import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductAttributeBoolean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BooleanAttributeTO extends AttributeTO<Boolean> {

	private String description;
	
	public BooleanAttributeTO() {
       super(ProductAttributeBoolean.class);
    }
	
	@Override
	public ProductAttributeBoolean buildEntity(ProductArticle pa) {
	    ProductAttributeBoolean entity = (ProductAttributeBoolean) super.buildEntity(pa);
	    entity.setDescription(description);
	    return entity;
	}
}
