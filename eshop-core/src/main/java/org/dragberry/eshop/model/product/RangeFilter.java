package org.dragberry.eshop.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RangeFilter extends Filter {
	
	private static final String TEMPLATE_NAME = "range-filter";
	
	private static final String DEFAULT_MASK = "# ##0.00";
	
	private String mask;
	
	private String fromId;
	
	private String toId;

	public RangeFilter() {
		super(TEMPLATE_NAME);
		this.mask = DEFAULT_MASK;
	}
}