package org.dragberry.eshop.utils;

import java.util.Set;
import java.util.stream.Collectors;

import org.dragberry.eshop.dal.entity.Product;
import org.dragberry.eshop.dal.entity.ProductArticleOption;
import org.dragberry.eshop.model.common.KeyValue;

public class ProductTitleBuilder {
	
	private ProductTitleBuilder() {}
	
	public static String buildFullTitle(String title, Set<KeyValue> options) {
		return title + options.stream().map(option -> option.getKey() + ": " + option.getValue()).collect(Collectors.joining("; ", " (", ")"));
	}

	public static String buildFullTitle(Product product) {
		return product.getProductArticle().getTitle() + product.getOptions().stream().map(option -> option.getName() + ": " + option.getValue()).collect(Collectors.joining("; ", " (", ")"));
	}
	
	public static String buildOptionsTitle(Product product) {
		return product.getOptions().stream().map(ProductArticleOption::getValue).collect(Collectors.joining("; "));
	}
}
