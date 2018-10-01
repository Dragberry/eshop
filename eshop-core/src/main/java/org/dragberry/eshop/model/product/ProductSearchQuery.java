package org.dragberry.eshop.model.product;

import java.util.Map;

import lombok.Data;

@Data
public class ProductSearchQuery {

	private String categoryReference;
	
	private String categoryName;
	
	private Map<String, String[]> searchParams;
	
}
