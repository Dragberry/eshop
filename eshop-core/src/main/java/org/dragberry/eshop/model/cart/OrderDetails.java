package org.dragberry.eshop.model.cart;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.dragberry.eshop.model.payment.PaymentMethodTO;
import org.dragberry.eshop.model.shipping.ShippingMethodTO;

import lombok.Data;

@Data
public class OrderDetails {

    private Long id;
    
	private String phone;
	
	private String fullName;
	
	private String address;
	
	private String comment;
	
	private String email;
	
	private BigDecimal totalProductAmount;
	
	private BigDecimal shippingCost;
	
	private BigDecimal totalAmount;
	
	private ShippingMethodTO shippingMethod;
	
	private PaymentMethodTO paymentMethod;
	
	private Map<CapturedProduct, CapturedProductState> products = new HashMap<>();
	
	public void clear() {
		this.phone = null;
		this.fullName = null;
		this.address = null;
		this.comment = null;
		this.email = null;
		this.totalProductAmount = null;
		this.shippingCost = null;
		this.totalAmount= null;
		this.shippingMethod = null;
		this.paymentMethod = null;
		this.products.clear();
	}
}
