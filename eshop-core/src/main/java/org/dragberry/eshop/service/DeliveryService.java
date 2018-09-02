package org.dragberry.eshop.service;

import java.util.List;

import org.dragberry.eshop.model.delivery.DeliveryMethod;

public interface DeliveryService {
	
	List<DeliveryMethod> getDeliveryMethods();

}
