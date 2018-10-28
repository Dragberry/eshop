package org.dragberry.eshop.dal.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.dragberry.eshop.dal.entity.Order.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDTO {

	private Long id;

	private LocalDateTime date;
	
	private BigDecimal totalAmount;

	private String fullName;
	
	private String phone;
	
	private String address;
	
	private String comment;
	
	private String email;
	
	private Boolean paid;
	
	private Long shippingMethodId;
	
	private String shippingMethod;
	
	private Long paymentMethodId;
	
	private String paymentMethod;
	
	private OrderStatus status;
	
	private Long version;
	
}
