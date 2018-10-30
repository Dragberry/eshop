package org.dragberry.eshop.cms.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.dragberry.eshop.dal.entity.Order.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderTO implements Serializable {

	private static final long serialVersionUID = 2253922422700962998L;

	private Long id;
	
	private String phone;
	
	private BigDecimal totalAmount;
	
	private String fullName;
	
	private String address;
	
	private String comment;
	
	private String email;
	
	private Boolean paid = Boolean.FALSE;
	
	private Long shippingMethodId;
	
	private String shippingMethod;
	
	private Long paymentMethodId;
	
	private String paymentMethod;
	
	private LocalDateTime date;
	
	private OrderStatus status;
	
	private Long version;
	
	
}
