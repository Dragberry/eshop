package org.dragberry.eshop.model.product;

import lombok.Data;

@Data
public class ProductCategory {

	private Long id;
	
	private String reference;
	
	private String name;

	public ProductCategory(Long id, String reference, String name) {
		this.id = id;
		this.reference = reference;
		this.name = name;
	}

	public ProductCategory() {}
	
}
