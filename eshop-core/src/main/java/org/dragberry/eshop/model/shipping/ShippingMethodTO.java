package org.dragberry.eshop.model.shipping;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShippingMethodTO {

	private Long id;
	
	private String name;

	private String description;
	
	private BigDecimal cost;

}
