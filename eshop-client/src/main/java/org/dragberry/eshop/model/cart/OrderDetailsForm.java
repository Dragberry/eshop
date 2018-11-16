package org.dragberry.eshop.model.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailsForm {
    
	private String phone;
	
	private String fullName;
	
	private String address;
	
	private String comment;
	
	private String email;
	
	private Long shippingMethod;
	
	private Long paymentMethod;
	
}
