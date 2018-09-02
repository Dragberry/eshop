package org.dragberry.eshop.model.delivery;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryMethod {

	private Long id;
	
	private String name;

	private String description;
	
	private BigDecimal price;

}
