package org.dragberry.eshop.cms.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.dragberry.eshop.dal.entity.Order.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailsTO implements Serializable {

	private static final long serialVersionUID = 2253922422700962998L;

	private Long id;
	
	private LocalDateTime orderDate;
	
	private Long version;
	
	
	private String phone;
	
	private String fullName;
	
	private String address;
	
	private String email;

	private String comment;
	
	private String customerComment;
	
	private String shopComment;
	
	private LocalDateTime deliveryDateFrom;
	
	private LocalDateTime deliveryDateTo;
	
	
	private Long paymentMethodId;
	
	private Long shippingMethodId;
	
	private BigDecimal totalProductAmount;
	
	private BigDecimal shippingCost;
	
    private BigDecimal totalAmount;
    
    private Boolean paid;
	
	private OrderStatus status;
	
	
	private List<OrderItemTO> items;
}
