package org.dragberry.eshop.model.cart;

import lombok.Data;

@Data
public class OrderDetails {

	private String phone;
	
	private String fullName;
	
	private String address;
	
	private String comment;
	
	private String email;
	
	private Long deliveryMethod;
	
	private Long paymentMethod;
	
	public void clear() {
		this.phone = null;
		this.fullName = null;
		this.address = null;
		this.comment = null;
		this.email = null;
		this.deliveryMethod = null;
		this.paymentMethod = null;
	}
}
