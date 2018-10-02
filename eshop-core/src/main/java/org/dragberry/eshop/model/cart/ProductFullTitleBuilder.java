package org.dragberry.eshop.model.cart;

import java.util.Set;
import java.util.stream.Collectors;

import org.dragberry.eshop.model.common.KeyValue;

public class ProductFullTitleBuilder {
	
	private ProductFullTitleBuilder() {}
	
	public static String buildFullTitle(String title, Set<KeyValue> options) {
		return title + options.stream().map(option -> option.getKey() + ": " + option.getValue()).collect(Collectors.joining("; ", " (", ")"));
	}

}
