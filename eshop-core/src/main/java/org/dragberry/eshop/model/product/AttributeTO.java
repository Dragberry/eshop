package org.dragberry.eshop.model.product;

import org.dragberry.eshop.dal.entity.ProductArticle;
import org.dragberry.eshop.dal.entity.ProductAttribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class AttributeTO<T> {
    
    private final Class<? extends ProductAttribute<T>> type;
	
	private Long id;
	
	private String name;
	
	private String group;
	
	private T value;
	
	private Integer order;

	protected AttributeTO(Class<? extends ProductAttribute<T>> type) {
	    this.type = type;
	}
	
	public ProductAttribute<T> buildEntity(ProductArticle pa) {
	    ProductAttribute<T> entity;
        try {
            entity = type.getConstructor().newInstance();
        } catch (Exception exc) {
            throw new RuntimeException("An error has occurred while instantiation of " + type);
        }
        entity.setEntityKey(id);
        entity.setProductArticle(pa);
        entity.setName(name);
        entity.setGroup(group);
        entity.setOrder(order);
        entity.setValue(value);
        return entity;
	}
	
	protected T copyValue() {
	    return value;
	}
}
