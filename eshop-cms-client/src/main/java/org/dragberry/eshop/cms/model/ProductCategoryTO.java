package org.dragberry.eshop.cms.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCategoryTO {

	private Long id;
	
	private String name;
	
	private String referecence;
	
	private List<ProductCategoryTO> categories = new ArrayList<>();
}
