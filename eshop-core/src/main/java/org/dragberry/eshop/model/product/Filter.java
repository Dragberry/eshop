package org.dragberry.eshop.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Filter {
	
	private static final String LOCATION = "pages/products/list/product-filters :: ";
	
	private String id;
	
	private String name;
	
	private final String template;
	
	public Filter(String template) {
		this.template = LOCATION + template;
	}
}