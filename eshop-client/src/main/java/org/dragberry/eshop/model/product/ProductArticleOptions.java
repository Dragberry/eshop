package org.dragberry.eshop.model.product;

import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
@Setter
public class ProductArticleOptions {

	private String title;
	
	private Map<Long, ProductOptionDetails> options = new LinkedHashMap<>();
}
