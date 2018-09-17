package org.dragberry.eshop.model.product;

import java.util.ArrayList;
import java.util.List;

import org.dragberry.eshop.model.common.KeyValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupFilter extends Filter {

	private static final String TEMPLATE_NAME = "group-filter";
	
	private List<KeyValue> attributes = new ArrayList<>();

	public GroupFilter() {
		super(TEMPLATE_NAME);
	}
}