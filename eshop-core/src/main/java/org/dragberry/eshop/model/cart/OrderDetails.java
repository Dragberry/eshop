package org.dragberry.eshop.model.cart;

import java.util.HashMap;
import java.util.Map;

import org.dragberry.eshop.model.delivery.DeliveryMethod;
import org.dragberry.eshop.model.payment.PaymentMethod;

import lombok.Data;

@Data
public class OrderDetails {

    private Long id;
    
	private String phone;
	
	private String fullName;
	
	private String address;
	
	private String comment;
	
	private String email;
	
	private DeliveryMethod deliveryMethod;
	
	private PaymentMethod paymentMethod;
	
	private Map<CapturedProduct, CapturedProductState> products = new HashMap<>();
	
	public void clear() {
		this.phone = null;
		this.fullName = null;
		this.address = null;
		this.comment = null;
		this.email = null;
		this.deliveryMethod = null;
		this.paymentMethod = null;
		this.products.clear();
	}
}
