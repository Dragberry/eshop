package org.dragberry.eshop.dal.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT_ATTRIBUTE_NUMERIC")
@Getter
@Setter
public class ProductAttributeNumeric  extends ProductAttribute<BigDecimal> {

    @Column(name = "VALUE")
    private BigDecimal value;
    
    @Column(name = "UNIT")
    private String unit;
    
    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
    public static ProductAttributeNumeric of(ProductArticle productArticle, String name, BigDecimal value, String unit) {
    	var entity = new ProductAttributeNumeric();
    	entity.setProductArticle(productArticle);
    	entity.setName(name);
    	entity.setValue(value);
    	entity.setUnit(unit);
    	return entity;
    }
}
