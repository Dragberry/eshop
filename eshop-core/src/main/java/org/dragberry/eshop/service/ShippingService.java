package org.dragberry.eshop.service;

import java.util.List;

import org.dragberry.eshop.model.shipping.ShippingMethod;

public interface ShippingService {
	
	List<ShippingMethod> getShippingMethods();

}
