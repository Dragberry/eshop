package org.dragberry.eshop.model.product;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RangeFilter extends Filter {
	
	private static final String TEMPLATE_NAME = "range-filter";
	
	private String mask;
	
	private BigDecimal from;
	
	private BigDecimal to;

	public RangeFilter() {
		super(TEMPLATE_NAME);
	}
}