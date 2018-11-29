package org.dragberry.eshop.model.product;

import java.math.BigDecimal;

import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductAttributeNumeric;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NumericAttributeTO extends AttributeTO<BigDecimal> {

	private String unit;
	
	public NumericAttributeTO() {
        super(ProductAttributeNumeric.class);
    }
	
	@Override
    public ProductAttributeNumeric buildEntity(ProductArticle pa) {
	    ProductAttributeNumeric entity = (ProductAttributeNumeric) super.buildEntity(pa);
        entity.setUnit(unit);
        return entity;
    }
}
