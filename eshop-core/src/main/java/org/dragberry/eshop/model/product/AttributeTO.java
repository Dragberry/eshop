package org.dragberry.eshop.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AttributeTO<T> {
    
    private final String type;
	
	private Long id;
	
	private String name;
	
	private String group;
	
	private T value;
	
	private Integer order;

	protected AttributeTO(String type) {
	    this.type = type;
	}
}
