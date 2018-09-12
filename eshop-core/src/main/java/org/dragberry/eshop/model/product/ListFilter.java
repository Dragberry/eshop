package org.dragberry.eshop.model.product;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListFilter extends Filter {

	private static final String TEMPLATE_NAME = "list-filter";
	
	private List<String> attributes = new ArrayList<>();

	public ListFilter() {
		super(TEMPLATE_NAME);
	}
}