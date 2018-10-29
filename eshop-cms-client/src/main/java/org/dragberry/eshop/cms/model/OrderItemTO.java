package org.dragberry.eshop.cms.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemTO {

	private Long id;
	
	private BigDecimal price;
	
	private Integer quantity;
	
	private BigDecimal totalAmount;
	
	private OrderProductTO product;
	
	private Long version;
}
