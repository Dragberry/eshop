package org.dragberry.eshop.cms.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCategoryTO {

	private Long id;
	
	private String name;
	
	private List<ProductCategoryTO> categories;
}
