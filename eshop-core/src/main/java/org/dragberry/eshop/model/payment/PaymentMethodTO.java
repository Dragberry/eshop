package org.dragberry.eshop.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMethodTO {

	private Long id;
	
	private String name;
	
	private String description;
}
