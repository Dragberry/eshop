package org.dragberry.eshop.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooleanAttributeTO extends AttributeTO<Boolean> {

	private String description;
	
	public BooleanAttributeTO() {
       super("BooleanAttribute");
    }
}
