package org.dragberry.eshop.model.product;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ProductCategory {

	private Long id;
	
	private String reference;
	
	private String name;
	
	private List<Filter> filters = new ArrayList<>();

	public ProductCategory(Long id, String reference, String name) {
		this.id = id;
		this.reference = reference;
		this.name = name;
	}

	public ProductCategory() {}
	
}