package org.dragberry.eshop.cms.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.dragberry.eshop.dal.entity.Order.OrderStatus;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCmsModel implements Serializable {

	private static final long serialVersionUID = 2253922422700962998L;

	private Long id;
	
	private String phone;
	
	private String fullName;
	
	private String address;
	
	private String comment;
	
	private String email;
	
	private Long shippingMethodId;
	
	private String shippingMethod;
	
	private Long paymentMethodId;
	
	private String paymentMethod;
	
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime date;
	
	private OrderStatus status;
	
	private Long version;
}
